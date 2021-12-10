package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.components.confs.FileConfig;
import com.romanm.jwtservicedata.configs.auth.TestSecurityConfiguration;
import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = {"test"})
@Import(value = {TestSecurityConfiguration.class})
@EnableConfigurationProperties(value = FileConfig.class)
@TestPropertySource("classpath:/config/filescfg-test.properties")
public class FileUploadControllerIntegrationTest {
    private final static String MSG_SAVE_SINGLE_FILE = "Try to save file '%s'! Got status: '%s'!";
    private final static String FILE_TEST_PREFIX = "/test/";

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private FileConfig fileConfig;




    @Before
    public void initConfig() {
        log.info(MessageConstants.prefixMsg("initConfig()"));
    }

    /**
     * Получение тела запроса типа form-data для сохранения одного файла
     * @param filePath String
     * @param userId String
     * @return MultiValueMap<String, HttpEntity<?>>
     */
    private MultiValueMap<String, HttpEntity<?>> fromSingleFile(String filePath, String userId) {
        ClassPathResource res = new ClassPathResource(filePath);
        File file = new File(res.getPath());

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part(Api.PARAM_USER_ID, userId);
        builder.part("file", file.getPath().getBytes()).header("Content-Disposition", "form-data; name=file; filename="+file.getName());
        return builder.build();

    }

    /**
     * Конечная точка сохранения одиночного файла
     * @param fileName String
     * @param userId String
     */
    public FileStatus saveSingleFile(String fileName, String userId) {
        return this.webTestClient
                .post()
                .uri(Api.API_PREFIX+Api.API_USER_IMAGES)
                .body(BodyInserters.fromMultipartData(this.fromSingleFile(FILE_TEST_PREFIX+fileName, userId)))
                .exchange()
                .expectBody(FileStatus.class).returnResult().getResponseBody();
    }

    /**
     * Получение тела запроса типа form-data для удаления одного файла
     * @param fileName String
     * @param userId String
     * @return MultiValueMap<String, HttpEntity<?>>
     */
    private MultiValueMap<String, HttpEntity<?>> fromSimpleParams(String fileName, String userId) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part(Api.PARAM_USER_ID, userId);
        builder.part(Api.PARAM_FILE_ID, fileName);
        return builder.build();
    }

    /**
     * Метод удаления одиночного файла (успешный тест)
     * @param fileName String
     * @param userId String
     */
    private void removeSingleFileAccepted(String fileName, String userId) {
        this.webTestClient
                .delete()
                .uri(uriBuilder -> (
                        uriBuilder.path(Api.API_PREFIX+Api.API_USER_IMAGES)
                                .queryParam(Api.PARAM_FILE_ID, fileName)
                                .queryParam(Api.PARAM_USER_ID, userId)
                                .build()))
                .exchange()
                .expectStatus()
                .isAccepted();
    }

    /**
     * Метод удаления одиночного файла (неуспешный тест)
     * @param fileName String
     * @param userId String
     */
    private void removeSingleFileNotModified(String fileName, String userId) {
        this.webTestClient
                .delete()
                .uri(uriBuilder -> (
                        uriBuilder.path(Api.API_PREFIX+Api.API_USER_IMAGES)
                                .queryParam(Api.PARAM_FILE_ID, fileName)
                                .queryParam(Api.PARAM_USER_ID, userId)
                                .build()))
                .exchange()
                .expectStatus()
                .isNotModified();
    }



    @Test
    public void testSaveAndDeleteSingleFile() {
        String userId = "2002";

        /*Сохранит и удалит файл file1.png*/
        String fileNamePng = "file1.png";
        FileStatus statusOk1 = this.saveSingleFile(fileNamePng, userId);
        log.info(MessageConstants.prefixMsg(String.format(MSG_SAVE_SINGLE_FILE, statusOk1.getFileName(), statusOk1.isSaved())));
        Assert.assertTrue(statusOk1.isSaved());
        //Удаление файла с кодом 202
        this.removeSingleFileAccepted(fileNamePng, userId);
        //Повторное неудачное удаление с кодом 304
        this.removeSingleFileNotModified(fileNamePng, userId);

        /*Сохранит и удалит файл file2.jpg*/
        String fileNameJpg = "file2.jpg";
        FileStatus statusOk2 = this.saveSingleFile(fileNameJpg, userId);
        log.info(MessageConstants.prefixMsg(String.format(MSG_SAVE_SINGLE_FILE, statusOk2.getFileName(), statusOk2.isSaved())));
        Assert.assertTrue(statusOk2.isSaved());
        //Удаление файла с кодом 202
        this.removeSingleFileAccepted(fileNameJpg, userId);
        //Повторное неудачное удаление с кодом 304
        this.removeSingleFileNotModified(fileNameJpg, userId);

        /*Сохранит и удалит файл file3.pdf*/
        String fileNamePdf = "file3.pdf";
        FileStatus statusNotOkPdf = this.saveSingleFile(fileNamePdf, userId);
        log.info(MessageConstants.prefixMsg(String.format(MSG_SAVE_SINGLE_FILE, statusNotOkPdf.getFileName(), statusNotOkPdf.isSaved())));
        Assert.assertFalse(statusNotOkPdf.isSaved());
        //Повторное неудачное удаление с кодом 304
        this.removeSingleFileNotModified(fileNamePdf, userId);
    }
}