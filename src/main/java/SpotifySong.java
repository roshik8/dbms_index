import com.opencsv.bean.CsvBindByName;

import java.util.Objects;

public class SpotifySong {
    @CsvBindByName(column = "id")
    public Integer id;
    @CsvBindByName(column = "artist_name")
    private String artistName;
    @CsvBindByName(column = "track_name")
    public String trackName;
    @CsvBindByName(column = "track_id")
    private String trackId;
    @CsvBindByName(column = "popularity")
    private String popularity;
    @CsvBindByName(column = "year")
    private int year;
    @CsvBindByName(column = "genre")
    private String genre;
    @CsvBindByName(column = "danceability")
    public double danceability;
    @CsvBindByName(column = "energy")
    private double energy;
    @CsvBindByName(column = "key")
    private String key;
    @CsvBindByName(column = "loudness")
    private String loudness;
    @CsvBindByName(column = "mode")
    private String mode;
    @CsvBindByName(column = "speechiness")
    private String speechiness;
    @CsvBindByName(column = "acousticness")
    private double acousticness;
    @CsvBindByName(column = "instrumentalness")
    private double instrumentalness;
    @CsvBindByName(column = "liveness")
    private double liveness;
    @CsvBindByName(column = "valence")
    private double valence;
    @CsvBindByName(column = "tempo")
    private double tempo;
    @CsvBindByName(column = "duration_ms")
    private int durationMs;
    @CsvBindByName(column = "time_signature")
    private int timeSignature;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotifySong that = (SpotifySong) o;
        return year == that.year && Double.compare(that.danceability, danceability) == 0 && Double.compare(that.energy, energy) == 0 && Double.compare(that.acousticness, acousticness) == 0 && Double.compare(that.instrumentalness, instrumentalness) == 0 && Double.compare(that.liveness, liveness) == 0 && Double.compare(that.valence, valence) == 0 && Double.compare(that.tempo, tempo) == 0 && durationMs == that.durationMs && timeSignature == that.timeSignature && id.equals(that.id) && artistName.equals(that.artistName) && trackName.equals(that.trackName) && trackId.equals(that.trackId) && Objects.equals(popularity, that.popularity) && Objects.equals(genre, that.genre) && Objects.equals(key, that.key) && Objects.equals(loudness, that.loudness) && Objects.equals(mode, that.mode) && Objects.equals(speechiness, that.speechiness);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artistName, trackName, trackId, popularity, year, genre, danceability, energy, key, loudness, mode, speechiness, acousticness, instrumentalness, liveness, valence, tempo, durationMs, timeSignature);
    }

    @Override
    public String toString() {
        return "SpotifySong{" +
                "id=" + id +
                ", artistName='" + artistName + '\'' +
                ", trackName='" + trackName + '\'' +
                ", trackId='" + trackId + '\'' +
                ", popularity='" + popularity + '\'' +
                ", year=" + year +
                ", genre='" + genre + '\'' +
                ", danceability=" + danceability +
                ", energy=" + energy +
                ", key='" + key + '\'' +
                ", loudness='" + loudness + '\'' +
                ", mode='" + mode + '\'' +
                ", speechiness='" + speechiness + '\'' +
                ", acousticness=" + acousticness +
                ", instrumentalness=" + instrumentalness +
                ", liveness=" + liveness +
                ", valence=" + valence +
                ", tempo=" + tempo +
                ", durationMs=" + durationMs +
                ", timeSignature=" + timeSignature +
                '}';
    }
}
