package com.hanu.leaniverse.service;

import com.hanu.leaniverse.model.Video;

public interface VideoService {
    void addNewVideo(Video video);
    void deleteVideoById(int videoId);
}
