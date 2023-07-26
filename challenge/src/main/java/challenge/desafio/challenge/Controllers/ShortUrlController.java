package challenge.desafio.challenge.Controllers;

import challenge.desafio.challenge.Domain.Dtos.LongUrlDto;
import challenge.desafio.challenge.Domain.Dtos.RequestBodyDto;
import challenge.desafio.challenge.Domain.Models.ShortUrl;

import challenge.desafio.challenge.Exceptions.ShortUrlException;
import challenge.desafio.challenge.Services.ShortUrlServicesImpl;


import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.CacheEvict;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/")
@Log4j2

public class ShortUrlController {

    private final ShortUrlServicesImpl shortUrlServicesImpl;

    @Autowired
    public ShortUrlController(ShortUrlServicesImpl shortUrlServicesImpl) {
        this.shortUrlServicesImpl = shortUrlServicesImpl;
    }

    @Cacheable(value = "myCache")
    @GetMapping("api/url/get-created")
    public ResponseEntity<List<ShortUrl>> getAllUrlCreated() {

        List<ShortUrl> shortUrlList = shortUrlServicesImpl.getAmountOfUrl();

        if (!shortUrlList.isEmpty()) {
            return ResponseEntity.ok(shortUrlList);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("api/url/get-amount-created")
    public ResponseEntity<Integer> getAmountOfUrl() {

        int resp = shortUrlServicesImpl.getAmountOfUrl().size();

        if (resp >= 0) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("api/url/get-amount-inactives")
    public ResponseEntity<Integer> getAllInactivesUrl() {
        int resp = shortUrlServicesImpl.getAmountInactivesUrl();
        if (resp >= 0) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("api/url/get-amount-actives")
    public ResponseEntity<Integer> getAllActivesUrl() {
        var resp = shortUrlServicesImpl.getAmountActivesUrl();

        if (resp >= 0) {
            return ResponseEntity.ok(resp);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CacheEvict(value = "myCache", allEntries = true)
    @PutMapping("api/url/{url}/delete")
    public ResponseEntity<ShortUrl> logicDeleted(@PathVariable String url) {
        try {
            ShortUrl shortUrlDeleted = shortUrlServicesImpl.logicDeleted(url);
            return ResponseEntity.status(HttpStatus.CREATED).body(shortUrlDeleted);
        } catch (ShortUrlException e) {
            log.error(e.getDescription(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("api/url/create-short-url")
    public ResponseEntity<String> createShortUrl(@RequestBody LongUrlDto url) {
        try {
            String encriptedUrl = shortUrlServicesImpl.createShortUrl(url);
            return ResponseEntity.status(HttpStatus.CREATED).body(encriptedUrl);
        } catch (ShortUrlException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping("/{url}/{url2}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String url2) {
        log.info("get  original url is initalized");
        String resp = shortUrlServicesImpl.getOriginalUrl(url2);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(resp)).build();


    }

    @CacheEvict(value = "myCache", allEntries = true)
    @PutMapping("api/url/{url}/restore")
    public ResponseEntity<ShortUrl> restoreUrl(@PathVariable String url) {
        try {
            ShortUrl shortUrl = shortUrlServicesImpl.restoreUrl(url);
            return ResponseEntity.status(HttpStatus.CREATED).body(shortUrl);
        } catch (ShortUrlException e) {
            log.error(e.getDescription(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("api/url/active-info")
    public ResponseEntity<List<ShortUrl>> getAllInActiveUrlsInfo() {
        List<ShortUrl> shortUrlList = shortUrlServicesImpl.getAllInActiveUrlsInfo();
        return ResponseEntity.status(HttpStatus.OK).body(shortUrlList);
    }

    @GetMapping("api/url/inactive-info")
    public ResponseEntity<List<ShortUrl>> getAllActiveUrlsInfo() {
        List<ShortUrl> shortUrlList = shortUrlServicesImpl.getAllActiveUrlsInfo();
        if (!shortUrlList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(shortUrlList);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("api/url/get-short-url")
    public ResponseEntity<String> getShortUrl(@RequestBody RequestBodyDto id) {
        long idToSearch = id.getId();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(shortUrlServicesImpl.getShortUrl(idToSearch));
        } catch (ShortUrlException e) {
            log.error(e.getDescription(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping("seed")
    public String getSeed() {
        try {
            String seed = shortUrlServicesImpl.getSeed();
            return seed;
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error creando las seed";
        }

    }
}