package challenge.desafio.challenge.Domain.Services;

import challenge.desafio.challenge.Domain.Dtos.LongUrlDto;
import challenge.desafio.challenge.Domain.Models.ShortUrl;
import challenge.desafio.challenge.Exceptions.ShortUrlException;


import java.util.List;

public interface ShortUrlService {

    List<ShortUrl> getAmountOfUrl() throws ShortUrlException;

    String createShortUrl(LongUrlDto url) throws ShortUrlException;

    String getOriginalUrl(String url) throws ShortUrlException;

    int getAmountInactivesUrl() throws ShortUrlException;

    int getAmountActivesUrl() throws ShortUrlException;

    List<ShortUrl> getAllActiveUrlsInfo() throws ShortUrlException;

    List<ShortUrl> getAllInActiveUrlsInfo() throws ShortUrlException;


    ShortUrl logicDeleted(String url) throws ShortUrlException;

    ShortUrl restoreUrl(String url) throws ShortUrlException;

    String getShortUrl(long id) throws ShortUrlException;

    String getSeed() throws ShortUrlException;
}