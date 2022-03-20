package SongSearch;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Song {
    static final String STRING_FORMAT = "SongSearch.Song id=%d, artist=%d, name=%s, relase date=%tb-%td-%tY, length=%d, genre=%d";
    //static final DateFormat DATE_FORMAT = new SimpleDateFormat("mm-dd-yyyy");

    @JsonProperty("songID") private int songID;
    @JsonProperty("artistID") private int artistID;
    @JsonProperty("song_name") private String song_name;
    @JsonProperty("release_date") private Date release_date;
    @JsonProperty("length") private int length;
    @JsonProperty("genreID") private int genreID;

    /**
     * Create a song
     */
    public Song(@JsonProperty("songID") int songID, @JsonProperty("artistID") int artistID, @JsonProperty("song_name")
            String song_name, @JsonProperty("release_date") Date release_date, @JsonProperty("length") int length,
                @JsonProperty("genreID") int genreID){
        this.songID = songID;
        this.artistID = artistID;
        this.song_name = song_name;
        this.release_date = release_date;
        this.length = length;
        this.genreID = genreID;
    }

    public int getSongID(){
        return songID;
    }

    //public

//    private String getDate(){
//        String strDate = DATE_FORMAT.format(this.release_date);
//        return strDate;
//    }

//    public String toString(){
//        return String.format(STRING_FORMAT,songID,artistID, song_name,release_date,length,genreID);
//    }
}
