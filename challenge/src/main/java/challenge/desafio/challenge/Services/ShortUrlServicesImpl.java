package challenge.desafio.challenge.Services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import challenge.desafio.challenge.Domain.Dtos.LongUrlDto;
import challenge.desafio.challenge.Domain.Models.ShortUrl;
import challenge.desafio.challenge.Domain.Repository.ShortUrlRepository;
import challenge.desafio.challenge.Domain.Services.ShortUrlService;
import challenge.desafio.challenge.Exceptions.ShortUrlException;
import challenge.desafio.challenge.Utils.ConversionUrl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortUrlServicesImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private ConversionUrl conversionUrl = new ConversionUrl();

    private static final Logger logger = LogManager.getLogger(ShortUrl.class);

    @Autowired
    public ShortUrlServicesImpl(ShortUrlRepository shortUrlRepository) {
        this.shortUrlRepository = shortUrlRepository;
    }

    public List<ShortUrl> getAmountOfUrl() {
        try {
            return shortUrlRepository.findAll();
        } catch (Exception e) {
            System.err.println("Error en getAmountOfUrl: " + e);
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public String createShortUrl(LongUrlDto longUrlDto) throws ShortUrlException {
        logger.info("Iniciando creación de Url");
        Optional<ShortUrl> shortUrl = shortUrlRepository.findUrl(longUrlDto.getUrl());
        String longUrl = longUrlDto.getUrl();
        long startTime = System.currentTimeMillis();
        try {
            if (this.isMeliUrl(longUrl)) {
                if (!shortUrl.isPresent()) {
                    return this.urlEncryption(longUrl, startTime);
                } else {
                    throw new ShortUrlException("La url ya está almacenada en la base de datos y es http://localhost:8080/me.li/" + conversionUrl.encodeUrl(shortUrl.get().getId()));
                }
            } else {
                throw new ShortUrlException("Url no permitida, debe pertenecer al dominio 'MercadoLibre'");
            }
        } catch (ShortUrlException e) {
            throw new ShortUrlException("error.on.createShortUrl: ", e.getMessage(), e);
        }
    }

    public String getOriginalUrl(String url) {
        logger.info("Obteniendo url original");
        long startTime = System.currentTimeMillis();
        long id = conversionUrl.decodeUrl(url);
        Optional<ShortUrl> longUrl = shortUrlRepository.findById(id);
        if (longUrl.isPresent() && longUrl.get().getIs_active() == 1) {
            return "Url inactiva";
        } else {
            ShortUrl shortUrl = longUrl.get();
            shortUrl.setAmount_of_clicks(shortUrl.getAmount_of_clicks() + 1);
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            shortUrl.setRetrieved_time(executionTime);
            shortUrlRepository.save(shortUrl);
            System.out.println("Tiempo que tardó de obtención: " + executionTime);
            logger.info("Finalizado con éxito");
            return longUrl.get().getLong_url();
        }
    }

    public int getAmountInactivesUrl() {
        return shortUrlRepository.findAllByIsInActive().size();
    }

    public int getAmountActivesUrl() {
        List<ShortUrl> activeUrls = shortUrlRepository.findAllByIsActive();
        return activeUrls.size();
    }

    public List<ShortUrl> getAllActiveUrlsInfo() {
        return shortUrlRepository.findAllByIsActive();
    }

    public List<ShortUrl> getAllInActiveUrlsInfo() {
        return shortUrlRepository.findAllByIsInActive();
    }

    public ShortUrl logicDeleted(String url) throws ShortUrlException {
        try {
            long id = conversionUrl.decodeUrl(url);
            Optional<ShortUrl> urlToFind = shortUrlRepository.findById(id);
            if (urlToFind.isPresent()) {
                ShortUrl urlToDelete = urlToFind.get();
                urlToDelete.setDeleted_of_time(Date.valueOf(LocalDate.now()));
                urlToDelete.setIs_active(1);
                ShortUrl deletedUrl = shortUrlRepository.save(urlToDelete);
                return deletedUrl;
            } else {
                throw new ShortUrlException("URL no encontrada con el ID proporcionado");
            }
        } catch (ShortUrlException e) {
            throw new ShortUrlException("error.logicDeleted:", e.getMessage(), e);
        }
    }

    public ShortUrl restoreUrl(String url) throws ShortUrlException {
        try {
            long id = conversionUrl.decodeUrl(url);
            Optional<ShortUrl> urlToFind = shortUrlRepository.findById(id);
            if (urlToFind.isPresent()) {
                ShortUrl urlToRestore = urlToFind.get();
                urlToRestore.setIs_active(0);
                ShortUrl restoredUrl = shortUrlRepository.save(urlToRestore);
                return restoredUrl;
            } else {
                throw new ShortUrlException("Error, URL no encontrada");
            }
        } catch (ShortUrlException e) {
            throw new ShortUrlException("Error en restoreUrl: ", e.getMessage(), e);
        }
    }

    public String getShortUrl(long id) throws ShortUrlException {
        try {
            Optional<ShortUrl> originalUrl = shortUrlRepository.findById(id);
            if (originalUrl.isPresent()) {
                String shortUrl = conversionUrl.encodeUrl(originalUrl.get().getId());
                if (shortUrl != null) {
                    return shortUrl;
                }
            }
            throw new ShortUrlException("URL no encontrada con el ID proporcionado: " + id);
        } catch (ShortUrlException e) {
            throw new ShortUrlException("Error en getShortUrl: ", e.getMessage(), e);
        }
    }

    public String getSeed() throws ShortUrlException {
        long startTime = System.currentTimeMillis();

        String[] popularUrls = {
                "https://www.mercadolibre.com.ar/interruptor-miniatura-para-riel-din-chint-nxb-2-63-40a/p/MLA13661808#reco_item_pos=1&reco_backend=machinalis-homes-pdp-boos&reco_backend_type=function&reco_client=home_navigation-recommendations&reco_id=522b7a0c-798e-4954-8279-87dc98143dcf",
                "https://listado.mercadolibre.com.ar/mancuernas#D[A:mancuernas,L:undefined]",
                "https://www.mercadolibre.com.ar/ofertas/movetelibre23#DEAL_ID=MLA33037&S=MKT&V=1&T=TSB&L=MKTPLACE_PPS_CROSS_MOVETE_LIBRE&me.flow=-1&me.bu=3&me.audience=all&me.content_id=PPS13_MOVETELIBRE_banner_search&me.component_id=exhibitors_ml&me.logic=user_journey&me.position=0&me.bu_line=26",
                "https://www.mercadolibre.com.ar/interruptor-sica-limit-782225/p/MLA11289920#reco_item_pos=3&reco_backend=machinalis-homes-pdp-boos&reco_backend_type=function&reco_client=home_navigation-recommendations&reco_id=1e37c5f9-4224-44b9-b8a3-f967e4708a18",
                "https://articulo.mercadolibre.com.ar/MLA-1317236701-modulo-sensor-microonda-proximidad-presencia-radar-rcwl0516-_JM#reco_item_pos=1&reco_backend=machinalis-homes-pdp-boos&reco_backend_type=function&reco_client=home_navigation-trend-recommendations&reco_id=405fcf1d-03bc-4b6b-b8a6-d6af6665f77c",
                "https://articulo.mercadolibre.com.ar/MLA-1420686792-sensor-ultrasonido-medidor-distancia-arduino-hc-sr04-_JM#reco_item_pos=1&reco_backend=adv_hybrid_L2_brothers_cruella&reco_backend_type=low_level&reco_client=vip-pads-up&reco_id=62732d36-55fd-4ea6-aa9e-c8873466a401&is_advertising=true&ad_domain=VIPDESKTOP_UP&ad_position=2&ad_click_id=ZjFlNzQ4Y2ItMTQ5NS00NzE2LWFkNjAtM2JkZTk1ZmIxMTg4",
        };

        for (String url : popularUrls) {
            LongUrlDto longUrlDto = new LongUrlDto();
            longUrlDto.setUrl(url);
            this.createShortUrl(longUrlDto);
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        return "Ok:Seed ejecutado correctamente " + executionTime + "ms";
    }

    private String urlEncryption(String url, long startTime) {
        ShortUrl shortUrl = ShortUrl.builder()
                .long_url(url)
                .dateOfCreate(Date.valueOf(LocalDate.now()))
                .amount_of_clicks(0)
                .is_active(0)
                .build();

        ShortUrl shortUrlSaved = shortUrlRepository.save(shortUrl);
        String encriptUrl = conversionUrl.encodeUrl(shortUrlSaved.getId());
        shortUrlSaved.setShort_url("https://me.li/" + encriptUrl);
        shortUrlRepository.save(shortUrlSaved);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        shortUrlSaved.setCreation_time(executionTime);
        shortUrlRepository.save(shortUrlSaved);
        logger.info("Finalizando creación de Url");
        return "https://me.li/" + encriptUrl;
    }

    private boolean isMeliUrl(String url) {
        return (url.matches("^https://.*") && url.toLowerCase().contains("mercadolibre"));
    }
}