package ru.skillbox.diplom.services;

import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.DoubleRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.entities.CaptchaCode;
import ru.skillbox.diplom.repositories.CaptchaRepositori;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
@Transactional
public class CaptchaService {
    @Autowired
    private CaptchaRepositori captchaRepositori;

    public CaptchaCode genAndSaveCaptcha() {
        long deltaTime = 3_600_000;

        CaptchaCode captchaCode = null;
        ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
        RandomWordFactory wordFactory = new RandomWordFactory();
        wordFactory.setCharacters("023456789");
        RandomFontFactory fontFactory = new RandomFontFactory();
        fontFactory.setMaxSize(30);
        fontFactory.setMinSize(25);
        cs.setHeight(56);
        cs.setWidth(103);
        cs.setWordFactory(wordFactory);
        cs.setFontFactory(fontFactory);
        cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
        cs.setFilterFactory(new DoubleRippleFilterFactory());

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            String decodeCode = EncoderHelper.getChallangeAndWriteImage(cs, "png", byteArrayOutputStream);
            String encodeCode = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            captchaCode = new CaptchaCode();
            captchaCode.setCode(decodeCode);
            captchaCode.setSecretCode(encodeCode);
            captchaCode.setTime(System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }

        captchaRepositori.save(captchaCode);
        captchaRepositori.deleteOldCaptca(System.currentTimeMillis() - deltaTime);

        return captchaCode;
    }
}
