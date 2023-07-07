package org.zerock.moamoa.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.notice.NoticeMapper;
import org.zerock.moamoa.domain.DTO.notice.NoticeReadUpdateRequest;
import org.zerock.moamoa.domain.DTO.notice.NoticeResponse;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.NoticeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;
    private final UserService userService;


    public NoticeService(NoticeRepository noticeRepository, NoticeMapper noticeMapper, UserService userService) {
        this.noticeRepository = noticeRepository;
        this.noticeMapper = noticeMapper;
        this.userService = userService;
    }

    public NoticeResponse findOne(Long id) {
        return noticeMapper.toDto(findById(id));
    }

    public Notice findById(Long id) {
        return this.noticeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTICE_NOT_FOUND));
    }

    public List<Notice> findAll() {
        return this.noticeRepository.findAll();
    }

    public NoticeResponse saveNotice(NoticeSaveRequest request, Long uid) {
        Notice notice = noticeMapper.toEntity((request));
        User user = userService.findById(uid);
        notice.setUserNotice(user);
        return noticeMapper.toDto(noticeRepository.save(notice));
    }

    public void removeNotice(Long id) {
        // 알림 완전삭제 X, 유저의 noticeList에서만 사라지면 될듯
        Optional<Notice> noticeOptional = noticeRepository.findById(id);
        noticeOptional.ifPresent(Notice::removeUserNotice); // = (notice -> {notice.removeUserNotice();})
    }

    // 읽을 시 상태 변경
    @Transactional
    public NoticeResponse updateRead(NoticeReadUpdateRequest NR) {
        Notice temp = findById(NR.getId());
        temp.updateRead(true);
        return noticeMapper.toDto(temp);
    }


    // 알림 조회
    public List<NoticeResponse> getReminderNotices(Long userId) {
        List<Notice> notices = userService.findById(userId).getNotices();
        return noticeMapper.toDtoList(notices);
    }
}
