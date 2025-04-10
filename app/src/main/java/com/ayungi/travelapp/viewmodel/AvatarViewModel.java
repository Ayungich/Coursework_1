package com.ayungi.travelapp.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ayungi.travelapp.model.data.responses.AvatarUploadResponseDto;
import com.ayungi.travelapp.model.repository.AvatarRepository;
import com.ayungi.travelapp.utils.Resource;

import java.io.File;

public class AvatarViewModel extends ViewModel {

    private final AvatarRepository avatarRepository;

    public AvatarViewModel() {
        avatarRepository = AvatarRepository.getInstance();
    }

    public LiveData<Resource<AvatarUploadResponseDto>> uploadAvatar(long userId, File file) {
        return avatarRepository.uploadAvatar(userId, file);
    }

    public LiveData<Resource<Bitmap>> getAvatar(Long avatarId) {
        return avatarRepository.getAvatar(avatarId);
    }
}
