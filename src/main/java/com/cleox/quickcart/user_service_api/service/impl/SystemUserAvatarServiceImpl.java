package com.cleox.quickcart.user_service_api.service.impl;

import com.cleox.quickcart.user_service_api.dto.request.SystemUserAvatarRequestDto;
import com.cleox.quickcart.user_service_api.entity.FileResource;
import com.cleox.quickcart.user_service_api.entity.User;
import com.cleox.quickcart.user_service_api.entity.UserAvatar;
import com.cleox.quickcart.user_service_api.exception.EntryNotFoundException;
import com.cleox.quickcart.user_service_api.exception.InternalServerException;
import com.cleox.quickcart.user_service_api.repo.SystemUserAvatarRepo;
import com.cleox.quickcart.user_service_api.repo.UserRepo;
import com.cleox.quickcart.user_service_api.service.FileService;
import com.cleox.quickcart.user_service_api.service.SystemUserAvatarService;
import com.cleox.quickcart.user_service_api.util.CommonFileSavedBinaryDataDTO;
import com.cleox.quickcart.user_service_api.util.FileDataExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SystemUserAvatarServiceImpl implements SystemUserAvatarService {

    private final SystemUserAvatarRepo systemUserAvatarRepo;
    private final UserRepo systemUserRepo;
    private final FileService fileService;
    private final FileDataExtractor fileDataExtractor;

    @Value("bucket")
    private String bucketName;

    @Override
    public void createSystemUserAvatar(SystemUserAvatarRequestDto dto, String email, MultipartFile file) throws Exception {
        CommonFileSavedBinaryDataDTO resource=null;
        Optional<User> selectedUser=systemUserRepo.findByUsername(email);
        if(selectedUser.isEmpty()){
            throw new EntryNotFoundException("User not found");
        }

        Optional<UserAvatar> selectedAvatar=systemUserAvatarRepo.findById(selectedUser.get().getUserId());
        if(selectedAvatar.isPresent()){
            try{
                try{
                    fileService.deleteResource(bucketName,"avatar/"+selectedUser.get().getUserId()+"/resource/",fileDataExtractor.byteArrayToString(selectedAvatar.get().getFileResource().getFileName()));
                } catch (Exception e) {
                        throw new InternalServerException("Failed to delete existing avatar resource directory");
                }

                resource=fileService.createResource(file,"avatar/"+selectedUser.get().getUserId()+"/resource/",bucketName);

                selectedAvatar.get().setFileResource(
                        new FileResource(
                                fileDataExtractor.blobToByteArray(resource.getFileName()),
                                fileDataExtractor.blobToByteArray(resource.getResourceUrl()),
                                fileDataExtractor.blobToByteArray(resource.getHash()),
                                resource.getDirectory().getBytes()
                        )
                );
                systemUserAvatarRepo.save(selectedAvatar.get());
            } catch (Exception e) {
                assert resource != null;
                fileService.deleteResource(bucketName,
                        resource.getDirectory(),fileDataExtractor.extractActualFileName(
                                new InputStreamReader(
                                        resource.getFileName().getBinaryStream()
                                )
                        ));
                fileService.deleteResource(bucketName,"avatar/"+selectedUser.get().getUserId()+"/resource/",fileDataExtractor.byteArrayToString(selectedAvatar.get().getFileResource().getFileName()));
                throw new InternalServerException("Something went wrong");
            }
        }else{
            try{
                resource=fileService.createResource(file,"avatar/"+selectedUser.get().getUserId()+"/resource/",bucketName);
                UserAvatar buildAvatar=UserAvatar.builder()
                        .avatarId(UUID.randomUUID().toString())
                        .fileResource(
                                new FileResource(
                                        fileDataExtractor.blobToByteArray(resource.getFileName()),
                                        fileDataExtractor.blobToByteArray(resource.getResourceUrl()),
                                        fileDataExtractor.blobToByteArray(resource.getHash()),
                                        resource.getDirectory().getBytes()
                                )
                        ).user(selectedUser.get()).build();
                systemUserAvatarRepo.save(buildAvatar);
            } catch (Exception e) {
                assert resource != null;
                fileService.deleteResource(bucketName,
                        resource.getDirectory(),fileDataExtractor.extractActualFileName(
                                new InputStreamReader(
                                        resource.getFileName().getBinaryStream())
                                )
                        );
                throw new InternalServerException("Something went wrong");
            }

        }
    }
}
