package com.hanu.leaniverse.service.user;

import com.hanu.leaniverse.model.Video;

public interface VideoService {
    void addNewVideo(Video video);
    void deleteVideoById(int videoId);
}
