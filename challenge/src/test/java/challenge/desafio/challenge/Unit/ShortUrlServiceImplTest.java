package challenge.desafio.challenge.Unit;

import challenge.desafio.challenge.Domain.Dtos.LongUrlDto;
import challenge.desafio.challenge.Domain.Models.ShortUrl;
import challenge.desafio.challenge.Domain.Repository.ShortUrlRepository;
import challenge.desafio.challenge.Exceptions.ShortUrlException;
import challenge.desafio.challenge.Services.ShortUrlServicesImpl;
import challenge.desafio.challenge.Utils.ConversionUrl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShortUrlServiceImplTest {

    @InjectMocks
    ShortUrlServicesImpl shortUrlServices;

    @Mock
    ShortUrlRepository shortUrlRepository;

    @Mock
    ConversionUrl conversionUrl;

    @Test
    void getAmountOfUrlTest() {
        shortUrlServices.getAmountOfUrl();
        verify(shortUrlRepository, atLeastOnce()).findAll();
    }

    @Test
    void createShortUrlTest() throws ShortUrlException {
        LongUrlDto longUrlDtoMock = mock(LongUrlDto.class);
        ShortUrl shortUrlMock = mock(ShortUrl.class);
        Optional<ShortUrl> shortUrlOptional = mock(Optional.class);

        when(longUrlDtoMock.getUrl()).thenReturn("https://www.mercadolibre.com.ar/");
        when(shortUrlRepository.findUrl(anyString())).thenReturn(shortUrlOptional);
        when(shortUrlOptional.isPresent()).thenReturn(false);
        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(shortUrlMock);
        when(shortUrlMock.getId()).thenReturn(1L);

        doNothing().when(shortUrlMock).setCreation_time(any());

        Assertions.assertEquals("https://me.li/b", shortUrlServices.createShortUrl(longUrlDtoMock));
    }

    @Test
    void createShortUrlWithUrlPresentTest() throws ShortUrlException {
        LongUrlDto longUrlDtoMock = mock(LongUrlDto.class);
        ShortUrl shortUrlMock = mock(ShortUrl.class);
        Optional<ShortUrl> shortUrlOptional = mock(Optional.class);

        when(longUrlDtoMock.getUrl()).thenReturn("https://www.mercadolibre.com.ar/");
        when(shortUrlRepository.findUrl(anyString())).thenReturn(shortUrlOptional);
        when(shortUrlOptional.isPresent()).thenReturn(true);
        when(shortUrlOptional.get()).thenReturn(shortUrlMock);

        when(shortUrlMock.getId()).thenReturn(1L);

        Assertions.assertThrows(ShortUrlException.class, () -> shortUrlServices.createShortUrl(longUrlDtoMock));
    }
    @Test
    void createShortUrlWithUrlIsNotMeliTest() throws ShortUrlException {
        LongUrlDto longUrlDtoMock = mock(LongUrlDto.class);
        ShortUrl shortUrlMock = mock(ShortUrl.class);
        Optional<ShortUrl> shortUrlOptional = mock(Optional.class);

        when(longUrlDtoMock.getUrl()).thenReturn("https://www.juan.com.ar/");
        when(shortUrlRepository.findUrl(anyString())).thenReturn(shortUrlOptional);

        Assertions.assertThrows(ShortUrlException.class, () -> shortUrlServices.createShortUrl(longUrlDtoMock));
    }


    @Test
    void getAllActiveUrlsInfoTest(){

       ShortUrlServicesImpl shortUrlServices1 = mock(ShortUrlServicesImpl.class);

        LongUrlDto longUrlDtoMock = mock(LongUrlDto.class);

        ShortUrl shortUrl = mock(ShortUrl.class);

        try {
            shortUrlServices1.createShortUrl(longUrlDtoMock);
            List<ShortUrl> activeUrlsList = new ArrayList<>();
            activeUrlsList.add(shortUrl);
            when(shortUrl.getIs_active()).thenReturn(0);
            for (ShortUrl url : activeUrlsList) {
                assertThat(url.getIs_active(), equalTo(0));
            }
        } catch (ShortUrlException e) {
            throw new RuntimeException(e);
        }


    }

}
