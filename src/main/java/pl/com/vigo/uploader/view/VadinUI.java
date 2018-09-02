package pl.com.vigo.uploader.view;

import com.vaadin.annotations.Title;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import server.droporchoose.UploadComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@SpringUI
@Title("Vaadin Uploader")
public class VadinUI extends UI {
    VerticalLayout content;
    @Override
    protected void init(VaadinRequest request) {
// Create the content root layout for the UI
        content = new VerticalLayout();
        setContent(content);
        UploadComponent uploadComponent = new
                UploadComponent(this::uploadReceived);
        uploadComponent.setStartedCallback(this::uploadStarted);
        uploadComponent.setProgressCallback(this::uploadProgress);
        uploadComponent.setFailedCallback(this::uploadFailed);
        uploadComponent.setWidth(500, Unit.PIXELS);
        uploadComponent.setHeight(300, Unit.PIXELS);
        uploadComponent.setCaption("File upload");
        content.addComponent(uploadComponent);
    }
    //Metoda uruchamia się po pomyślnym zakończeniu przesyłania
    private void uploadReceived(String fileName, Path file) {
        Notification.show("Przesyłanie zakończono: " + fileName,
                Notification.Type.HUMANIZED_MESSAGE);
        FileResource resource = new FileResource(file.toFile());
        Image image = new Image("test", resource);
        File path = new
                File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath()+"\\test.jpg")
                ;
        BufferedImage img;
        try {
            img = ImageIO.read(file.toFile());
            ImageIO.write(img, "JPG", path);
        } catch (IOException e1) {
// TODO Auto-generated catch block
            e1.printStackTrace();
        }
        content.addComponent(image);

    }
    //metoda uruchamia się od razu po zaczęciu przesyłania
    private void uploadStarted(String fileName) {
        Notification.show("Przesyłanie rozpoczęte: " + fileName,
                Notification.Type.HUMANIZED_MESSAGE);
    }
    //Metoda do prezentacji paska postępu, lub tekstu z progresem przesyłania
    private void uploadProgress(String fileName, long readBytes, long
            contentLength) {
        Notification.show(String.format("Postęp: %s : %d/%d", fileName,
                readBytes, contentLength),
                Notification.Type.TRAY_NOTIFICATION);
    }
    //Metoda to obsłużenia błędu podczas przesyłania
    private void uploadFailed(String fileName, Path file) {
        Notification.show("Przesyłanie nie udało się: " + fileName,
                Notification.Type.ERROR_MESSAGE);
    }
    }
