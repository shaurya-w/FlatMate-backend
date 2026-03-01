package com.example.mpr_backend_jan.service;

import com.example.mpr_backend_jan.dto.NoticeRequest;
import com.example.mpr_backend_jan.dto.NoticeResponse;
import com.example.mpr_backend_jan.model.Flat;
import com.example.mpr_backend_jan.model.Notice;
import com.example.mpr_backend_jan.model.Society;
import com.example.mpr_backend_jan.model.User;
import com.example.mpr_backend_jan.repository.NoticeRepository;
import com.example.mpr_backend_jan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    // Notice how FlatRepository is completely removed. We don't need it here anymore.

    @Transactional
    public NoticeResponse createNotice(NoticeRequest request, String authorEmail) {

        // 1. Fetch the authenticated user (Admin)
        // securely identifying the actor. We extract the email from the Spring Security context (which was validated via the session cookie)
        // and fetch the Admin's full record from the database.
        User author = userRepository.findByEmail(authorEmail)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        // 2. Fetch all flats owned by this user
        List<Flat> adminFlats = author.getFlats();
        if (adminFlats == null || adminFlats.isEmpty()) {
            throw new RuntimeException("Security Exception: Admin is not associated with any flat.");
        }

        // 3. Security Check: Verify the Admin owns a flat in the requested Society
        Society targetSociety = null;
        for (Flat flat : adminFlats) {
            if (flat.getSociety() != null && flat.getSociety().getId().equals(request.getSocietyId())) {
                targetSociety = flat.getSociety();
                break; // Authorization successful, we found a match!
            }
        }

        if (targetSociety == null) {
            throw new RuntimeException("Security Exception: You do not have permission to post notices to this society.");
        }

        // 4. Calculate expiration safely on the backend
        LocalDateTime expiration = LocalDateTime.now().plusDays(request.getDurationInDays());

        // 5. Build and save the entity
        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .society(targetSociety)  // Verified securely above
                .author(author)
                .tags(request.getTags())
                .expirationDate(expiration)
                .build();

        Notice savedNotice = noticeRepository.save(notice);
        return mapToResponse(savedNotice);
    }

    @Transactional(readOnly = true)
    public List<NoticeResponse> getActiveNotices(Long societyId) {
        return noticeRepository.findActiveNoticesForSociety(societyId, LocalDateTime.now())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private NoticeResponse mapToResponse(Notice notice) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .authorName(notice.getAuthor().getName())
                .tags(notice.getTags())
                .createdAt(notice.getCreatedAt())
                .expirationDate(notice.getExpirationDate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<NoticeResponse> getMyNoticeFeed(String userEmail) {
        // 1. Fetch the logged-in resident
        User resident = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Extract unique Society IDs from the resident's flats
        List<Long> mySocietyIds = resident.getFlats().stream()
                .filter(flat -> flat.getSociety() != null)
                .map(flat -> flat.getSociety().getId())
                .distinct()
                .collect(Collectors.toList());

        // 3. If the resident isn't part of any society, return an empty feed
        if (mySocietyIds.isEmpty()) {
            return List.of();
        }

        // 4. Fetch and map all active notices for their specific societies
        return noticeRepository.findActiveNoticesForMultipleSocieties(mySocietyIds, LocalDateTime.now())
                .stream()
                .map(this::mapToResponse) // Uses your existing mapping method
                .collect(Collectors.toList());
    }

}