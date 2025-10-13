package org.codeit.roomunion.user.adapter.out.event;

import org.codeit.roomunion.common.adapter.out.s3.AmazonS3Manager;
import org.codeit.roomunion.user.domain.event.ProfileImageUploadEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProfileImageUploadEventListener {

    private final AmazonS3Manager amazonS3Manager;

    public ProfileImageUploadEventListener(AmazonS3Manager amazonS3Manager) {
        this.amazonS3Manager = amazonS3Manager;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProfileImageUpload(ProfileImageUploadEvent event) {
        amazonS3Manager.uploadFile(event.getFilePath(), event.getFile());
    }
}