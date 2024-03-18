import org.json.simple.JSONObject;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAPP extends JFrame{
private JSONObject weatherData;
    public WeatherAPP(){

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500,700);
        setLayout(null);

        getContentPane().setBackground(new Color(64, 104, 203));
        addElements();
        setVisible(true);

    }

    private void addElements(){
    JTextField textField = new JTextField();
    textField.setBounds(10,10,430,50);
    textField.setFont(new Font("Indie Flower", Font.PLAIN,25));

    add(textField);

    JLabel cityName = new JLabel();
    cityName.setBounds(195,60,200,200);
    cityName.setFont(new Font("Cinzel",Font.BOLD,30));
    add(cityName);

        JLabel weatherForcastImage = new JLabel(loadImage("src/Pics/cloudy-2.png",250,250));
        weatherForcastImage.setBounds(25,200, 450,217);
        add(weatherForcastImage);

        JLabel temperature = new JLabel("20 F");
        temperature.setBounds(0,410,500,50);
        temperature.setFont(new Font("Indie Flower",Font.PLAIN,35));
        temperature.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperature);

        JLabel weatherText = new JLabel("Cloudy");
        weatherText.setBounds(0,440,500, 50);
        weatherText.setFont(new Font("Cinzel", Font.PLAIN,28));
        weatherText.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherText);

        JLabel humidityImage = new JLabel (loadImage3("src/Pics/humidity.png",100,100));
        humidityImage.setBounds(20,500,100,100);
        add(humidityImage);

        JLabel humidityText = new JLabel("Humidity");
        humidityText.setBounds(110,430,200,200);
        humidityText.setFont(new Font("Cinzel",Font.BOLD,15));
        add(humidityText);

        JLabel humidityPerText = new JLabel("100%");
        humidityPerText.setBounds(110,445,200,200);
        humidityPerText.setFont(new Font("Cinzel",Font.PLAIN,12));
        add(humidityPerText);

        JLabel windSpeedImage = new JLabel(loadImage3("src/Pics/wind.png",90,90));
        windSpeedImage.setBounds(300,500,100,100);
        add(windSpeedImage);

        JLabel windSpeedText = new JLabel("Windspeed");
        windSpeedText.setBounds(410,430,200,200);
        windSpeedText.setFont(new Font("Cinzel",Font.BOLD,15));
        add(windSpeedText);

        JLabel windSpeedStats = new JLabel("100 m/k");
        windSpeedStats.setBounds(410,445,200,200);
        windSpeedStats.setFont(new Font("Cinzel",Font.PLAIN,12));
        add(windSpeedStats);

        JButton search = new JButton(loadImage2("src/Pics/loupe.png", 40,40));
        search.setBounds(450,10,50,50);
       search.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {

               String userInput = textField.getText();

               if(userInput.replaceAll("\\s","").length() <= 0){
                   return;
               }
               weatherData = WeatherAppGeo.getWeatherData(userInput);

               String weatherCondition = (String) weatherData.get("weatercondition");

               switch(weatherCondition){
                   case "Clear":
                       weatherForcastImage.setIcon(loadImage("src/Pics/sun_1400310.png",250,250));
                       break;
                   case "Cloudy":
                       weatherForcastImage.setIcon(loadImage("src/Pics/cloudy-2.png",250,250));
                       break;
                   case "Rain":
                       weatherForcastImage.setIcon(loadImage("src/Pics/rain.png",250,250));
                       break;
                   case "Snow":
                       weatherForcastImage.setIcon(loadImage("src/Pics/snowy.png",250,250));
                       break;
               }

               double temperatureGEO = (double) weatherData.get("temperature");
               temperature.setText(temperatureGEO + "F");

               weatherText.setText(weatherCondition);

               long humidity = (long) weatherData.get("humidity");
               humidityPerText.setText(humidity + "%");

               double windspeed = (double) weatherData.get("windspeed");
               windSpeedStats.setText(windspeed + "MPH");

               String addCityName = textField.getText();
               String result = addCityName.substring(0,1).toUpperCase() + addCityName.substring(1).toLowerCase();
               cityName.setText(result);
           }
       });
        add(search);

    }
    private ImageIcon loadImage(String resourcPath, int width, int height){
        try{
            BufferedImage image = ImageIO.read(new File(resourcPath));
            Image scaledImage = image.getScaledInstance(width,height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);

        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Could not find image");
        return null;
    }

    private ImageIcon loadImage2(String resourcPath, int width, int height){
        try{
            BufferedImage image = ImageIO.read(new File(resourcPath));
            Image scaledImage = image.getScaledInstance(width,height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);

        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Could not find image");
        return null;
    }
    private ImageIcon loadImage3(String resourcPath, int width, int height){
        try{
            BufferedImage image = ImageIO.read(new File(resourcPath));
            Image scaledImage = image.getScaledInstance(width,height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);

        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Could not find image");
        return null;
    }
}
