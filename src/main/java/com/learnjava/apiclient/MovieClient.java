package com.learnjava.apiclient;

import com.learnjava.domain.movie.Movie;
import com.learnjava.domain.movie.MovieInfo;
import com.learnjava.domain.movie.Review;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.learnjava.util.CommonUtil.startTimer;
import static com.learnjava.util.CommonUtil.timeTaken;
import static org.apache.commons.lang3.StringUtils.join;

public class MovieClient {

    private final WebClient webClient;

    public MovieClient(WebClient webClient){
        this.webClient=webClient;
    }

    public Movie retrieveMovie(Long movieInfoId){
//        startTimer();
        //movieinfo
        var movieInfo=invokeMovieInfoService(movieInfoId);
        //review
        var reviews=invokeReviewService(movieInfoId);
//        timeTaken();
        return new Movie(movieInfo,reviews);
    }

    public List<Movie> retrieveMovies(List<Long> movieInfoIds){
        //movieinfo
        startTimer();
        var movies=movieInfoIds
                .stream()
                .map(this::retrieveMovie)
                .collect(Collectors.toList());
        timeTaken();
        return movies;
    }

    public List<Movie> retrieveMovies_CF(List<Long> movieInfoId){
        startTimer();
        var moviesCf=movieInfoId
                .stream()
                .map(this::retrieveMovie_CF)
                .collect(Collectors.toList());
        timeTaken();
        return moviesCf.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public List<Movie> retrieveMovies_CF_AllOf(List<Long> movieInfoId){
        startTimer();
        var moviesCf=movieInfoId
                .stream()
                .map(this::retrieveMovie_CF)
                .collect(Collectors.toList());
        var moviesAllOf=CompletableFuture.allOf(moviesCf.toArray(new CompletableFuture[moviesCf.size()]));
        timeTaken();
        return moviesAllOf
                .thenApply(v->
                        moviesCf
                                .stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList())).join();
    }

    public CompletableFuture<Movie> retrieveMovie_CF(Long movieInfoId){
//        startTimer();
        //movieinfo
        var movieInfo= CompletableFuture.supplyAsync(()->invokeMovieInfoService(movieInfoId));
        //review
        var reviews=CompletableFuture.supplyAsync(()->invokeReviewService(movieInfoId));
//        timeTaken();
        return movieInfo.thenCombine(reviews,Movie::new);
    }

    private MovieInfo invokeMovieInfoService(Long movieInfoId) {
        var movieInfoUrlPath="/v1/movie_infos/{movieInfoId}";
        return webClient
                .get()
                .uri(movieInfoUrlPath,movieInfoId)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .block();
    }


    private List<Review> invokeReviewService(Long movieInfoId) {
        //"?=1";
        var reviewUri=UriComponentsBuilder.fromUriString("/v1/reviews")
                .queryParam("movieInfoId",movieInfoId)
                .buildAndExpand()
                .toString();

        return webClient
                .get()
                .uri(reviewUri)
                .retrieve()
                .bodyToFlux(Review.class)
                .collectList()
                .block();
    }
}
