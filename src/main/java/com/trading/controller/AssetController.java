package com.trading.controller;

import org.springframework.web.bind.annotation.RestController;

import com.trading.model.Asset;
import com.trading.model.User;
import com.trading.service.AssetService;
import com.trading.service.UserService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/asset")
public class AssetController {

    private final AssetService assetService;
    private final UserService userService;

    public AssetController(final AssetService assetService, final UserService userService){
        this.assetService = assetService;
        this.userService = userService;
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@PathVariable Long assetId) throws Exception{
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(@PathVariable String coinId, @RequestHeader String jwt) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Asset asset= assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok(asset);
    }

    @GetMapping()
    public ResponseEntity<List<Asset>> getAssetsForUser(@RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        List<Asset> assets= assetService.getUsersAssets(user.getId());
        return ResponseEntity.ok(assets);
    }
    
}
