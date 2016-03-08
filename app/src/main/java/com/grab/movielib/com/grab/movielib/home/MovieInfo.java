package com.grab.movielib.com.grab.movielib.home;

import java.io.Serializable;

public class MovieInfo implements Serializable{
    public String id;
    public String title;
    public String originalTitle;
    public String language;
    public boolean video;
    public String posterPath;
    public String backdropPath;
    public int voteCount;
    public String overview;
    public String releaseDate;

    @Override
    public String toString() {
        return "\nid:: "+id
                +"\ntitle:: "+title
                +"\noriginalTitle:: "+originalTitle
                +"\nlanguage:: "+language
                +"\nposterPath:: "+posterPath
                +"\nbackdropPath:: "+backdropPath
                +"\noverview:: "+overview
                +"\nreleaseDate:: "+releaseDate
                +"\nvideo:: "+video
                +"\nvoteCount:: "+voteCount;
    }
}
