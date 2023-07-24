package com.learnjava.apiclient;

import com.learnjava.domain.movie.Movie;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieClientTest {

    WebClient webClient=WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    MovieClient movieClient=new MovieClient(webClient);

    @RepeatedTest(10)
    void retrieveMovie(){
        //given
        var movieId=1L;
        //when
        var movie=movieClient.retrieveMovie(movieId);
        //then
        assert movie!=null;
        assertEquals("Batman Begins",movie.getMovieInfo().getName());
        assert movie.getReviewList().size()==1;
    }

    @RepeatedTest(10)
    void retrieveMovie_CF(){
        //given
        var movieId=1L;
        //when
        var movie=movieClient.retrieveMovie_CF(movieId).join();
        System.out.println("Movie is :"+movie);
        //then
        assert movie!=null;
        assertEquals("Batman Begins",movie.getMovieInfo().getName());
        assert movie.getReviewList().size()==1;
    }


    @RepeatedTest(10)
    void retrieveMovies(){
        //given
        var movieIds= List.of(1L,2L,3L,4L,5L,6L,7L);
        //when
        var movies=movieClient.retrieveMovies(movieIds);
        //then
        assert movies!=null;
        assert movies.size()==7;
    }


    @RepeatedTest(10)
    void retrieveMovies_CF(){
        //given
        var movieIds= List.of(1L,2L,3L,4L,5L,6L,7L);
        //when
        var movies=movieClient.retrieveMovies_CF(movieIds);
        //then
        assert movies!=null;
        assert movies.size()==7;
    }

    @RepeatedTest(10)
    void retrieveMovies_CFAll_Of(){
        //given
        var movieIds= List.of(1L,2L,3L,4L,5L,6L,7L);
        //when
        var movies=movieClient.retrieveMovies_CF_AllOf(movieIds);
        //then
        assert movies!=null;
        assert movies.size()==7;
    }


}