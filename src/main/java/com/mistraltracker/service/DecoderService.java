package com.mistraltracker.service;

import com.mistraltracker.dto.LiveObjectsMessage;
import com.mistraltracker.model.WeatherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class DecoderService {

    private static final int ID_AIR_TEMP = 0x4097;
    private static final int ID_AIR_HUMIDITY = 0x4098;
    private static final int ID_LIGHT_INTENSITY = 0x4099;
    private static final int ID_UV_INDEX = 0x4100;
    private static final int ID_WIND_SPEED = 0x4191;
    private static final int ID_WIND_DIRECTION = 0x4193;
    private static final int ID_RAIN_GAUGE = 0x4194;
    private static final int ID_AIR_PRESSURE = 0x4195;
    private static final int ID_TEMP_ALT = 0x4A00;
    private static final int ID_WIND_ALT = 0x4B00;

    public WeatherData decoder(LiveObjectsMessage message) {
        WeatherData data = new WeatherData();

        data.setTimestamp(LocalDateTime.now());

        if (message.getMetadata() != null
                && message.getMetadata().getNetwork() != null
                && message.getMetadata().getNetwork().getLora() != null) {

            data.setDeviceEui(message.getMetadata().getNetwork().getLora().getDevEUI());
        }

        if(message.getValue() == null || message.getValue().getPayload() == null) {
            log.warn("Empty payload received!");
            return data;
        }

        String payload = message.getValue().getPayload();

        try {
            byte[] bytes = HexFormat.of().parseHex(payload);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            while(buffer.remaining() >= 2){
                buffer.order(ByteOrder.BIG_ENDIAN);
                int sensorId = Short.toUnsignedInt(buffer.getShort());
                buffer.order(ByteOrder.LITTLE_ENDIAN);

                switch (sensorId) {
                    case ID_AIR_TEMP:
                    case ID_TEMP_ALT:
                        if(buffer.remaining() >= 2) {
                            double temperature = buffer.getShort()/100.0;
                            data.setTemperature(temperature);
                        }
                        break;
                    case ID_AIR_HUMIDITY:
                        if(buffer.remaining() >= 2) {
                            double humidity = buffer.getShort()/100.0;
                            data.setHumidity(humidity);
                        }
                        break;
                    case ID_WIND_SPEED:
                    case ID_WIND_ALT:
                        if(buffer.remaining() >= 2) {
                            double windSpeed = buffer.getShort()/10.0;
                            data.setWindSpeed(windSpeed);
                        }
                        break;
                    case ID_WIND_DIRECTION:
                        if(buffer.remaining() >= 2) {
                            double windDirection = buffer.getShort();
                            data.setWindDirection(windDirection);
                        }
                        break;
                    case ID_RAIN_GAUGE:
                        if(buffer.remaining() >= 4) {
                            double rain = buffer.getInt()/1000.0;
                            data.setRainLevel(rain);
                        }
                        break;
                    case ID_LIGHT_INTENSITY:
                        if(buffer.remaining() >= 4) {
                            double light = buffer.getInt();
                            data.setLightIntensity(light);
                        }
                        break;
                    case ID_UV_INDEX:
                        if(buffer.remaining() >= 2) {
                            double uv = buffer.getShort()/10.0;
                            data.setUvIntensity(uv);
                        }
                        break;
                    default:
                        log.debug("Unknown id detected: {}", Integer.toHexString(sensorId));
                        if(buffer.remaining() > 0) {
                            buffer.get();
                        }
                        break;
                }
            }
            mistralScoreCalculation(data);
        } catch (Exception e) {
            log.error("Critical error while decoding the payload: {}", payload, e);
        }
        return data;
    }

    private void mistralScoreCalculation (WeatherData data) {
        double mistralScore = 100.0;
        Double temperature = data.getTemperature();
        Double wind = data.getWindSpeed();
        Double rain = data.getRainLevel();

        if(temperature == null) temperature = 20.0;
        if(wind == null) wind = 5.0;
        if(rain == null) rain = 0.0;

        if(rain > 0.5) {
            data.setMistralScore(0.0);
            data.setIsTerrasseOk(false);
            return;
        }

        if (temperature < 15) mistralScore -= (15 - temperature) * 5;
        if (temperature > 30) mistralScore -= (temperature - 30) * 4;

        if (wind > 20) mistralScore -= (wind - 20) * 2;
        if (wind > 50) mistralScore -= 30;

        mistralScore = Math.max(0, Math.min(100, mistralScore));

        data.setMistralScore(mistralScore);
        data.setIsTerrasseOk(mistralScore > 60);
    }
}
