import com.opencsv.bean.CsvToBeanBuilder;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CsvUtils {
    public static List<SpotifySong> getSongs() {
        Path path;
        {
            try {
                path = Paths.get(ClassLoader.getSystemResource("spotify_data.csv").toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Reader reader = Files.newBufferedReader(path);
            CsvToBeanBuilder<SpotifySong> csvToBeanBuilder = new CsvToBeanBuilder<SpotifySong>(reader).withIgnoreQuotations(true).withThrowExceptions(false).withType(SpotifySong.class);
            return csvToBeanBuilder.build().parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
