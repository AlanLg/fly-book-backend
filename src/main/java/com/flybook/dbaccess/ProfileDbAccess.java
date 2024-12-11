package com.flybook.dbaccess;

import com.flybook.model.dto.db.ProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("/db-access/profile")
public interface ProfileDbAccess {

    @PostMapping("/")
    ProfileDTO saveProfile(@RequestBody ProfileDTO profileDTO);
}
