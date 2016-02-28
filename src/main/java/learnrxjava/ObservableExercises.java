package learnrxjava;

import learnrxjava.types.BoxArt;
import learnrxjava.types.JSON;
import learnrxjava.types.Movies;
import rx.Observable;

public class ObservableExercises {

    /**
     * Return an Observable that emits a single value "Hello World!"
     * 
     * @return "Hello World!"
     */
    public Observable<String> exerciseHello() {
        //return Observable.error(new RuntimeException("Not Implemented"));
        return Observable.just("Hello World!");
    }

    /**
     * Transform the incoming Observable from "Hello" to "Hello [Name]" where [Name] is your name.
     *
     * @param "Hello Name!"
     */
    public Observable<String> exerciseMap(Observable<String> hello) {
        //return Observable.error(new RuntimeException("Not Implemented"));
        return Observable.just("Hello").map(n->n+" "+hello);
    }

    /**
     * Given a stream of numbers, choose the even ones and return a stream like:
     * <p>
     * 2-Even
     * 4-Even
     * 6-Even
     */
    public Observable<String> exerciseFilterMap(Observable<Integer> nums) {
        //return Observable.error(new RuntimeException("Not Implemented"));
        return nums.filter(n->(n&1)==0).map(n->n.toString()+"-Even");
    }

    /**
     * Flatten out all video in the stream of Movies into a stream of videoIDs
     * 
     * @param movieLists
     * @return Observable of Integers of Movies.videos.id
     */
    public Observable<Integer> exerciseConcatMap(Observable<Movies> movies) {
        //return Observable.error(new RuntimeException("Not Implemented"));
        return movies
                .concatMap(m->m.videos)
                .map(v->v.id);
    }

    /**
     * Flatten out all video in the stream of Movies into a stream of videoIDs
     * 
     * Use flatMap this time instead of concatMap. In Observable streams
     * it is almost always flatMap that is wanted, not concatMap as flatMap
     * uses merge instead of concat and allows multiple concurrent streams
     * whereas concat only does one at a time.
     * 
     * We'll see more about this later when we add concurrency.
     * 
     * @param movieLists
     * @return Observable of Integers of Movies.videos.id
     */
    public Observable<Integer> exerciseFlatMap(Observable<Movies> movies) {
        //return Observable.error(new RuntimeException("Not Implemented"));
        return movies.flatMap(m->m.videos.map(v->v.id));
    }

    /**
     * Retrieve the largest number.
     * 
     * Use reduce to select the maximum value in a list of numbers.
     */
    public Observable<Integer> exerciseReduce(Observable<Integer> nums) {
        //return Observable.error(new RuntimeException("Not Implemented"));
        return nums.reduce((a,b)->a>b?a:b);
    }

    /**
     * Retrieve the id, title, and <b>smallest</b> box art url for every video.
     * 
     * Now let's try combining reduce() with our other functions to build more complex queries.
     * 
     * This is a variation of the problem we solved earlier, where we retrieved the url of the boxart with a
     * width of 150px. This time we'll use reduce() instead of filter() to retrieve the _smallest_ box art in
     * the boxarts list.
     * 
     * See Exercise 19 of ComposableListExercises
     */
    public Observable<JSON> exerciseMovie(Observable<Movies> movies) {
        //return Observable.error(new RuntimeException("Not Implemented"));

        return movies.flatMap(m -> {
            return m.videos.flatMap(v -> {
                return v.boxarts.reduce((a, b) -> {
                    if(a.width * a.height > b.width * b.height) return b;
                    else return a;
                }).map(maxBoxart->json("id", v.id, "title", v.title, "boxart", maxBoxart.url));
            });
        });
    }


    /**
     * Combine 2 streams into pairs using zip.
     * 
     * a -> "one", "two", "red", "blue"
     * b -> "fish", "fish", "fish", "fish"
     * output -> "one fish", "two fish", "red fish", "blue fish"
     */
    public Observable<String> exerciseZip(Observable<String> a, Observable<String> b) {
        return Observable.zip(a,b,(x,y)->x+" "+y);
        //return Observable.error(new RuntimeException("Not Implemented"));
    }

    /**
     * Don't modify any values in the stream but do handle the error
     * and replace it with "default-value".
     */
    public Observable<String> handleError(Observable<String> data) {
        //return Observable.error(new RuntimeException("Not Implemented"));
        return data.onErrorResumeNext(Observable.just("default-value"));
    }

    /**
     * The data stream fails intermittently so return the stream
     * with retry capability.
     */
    public Observable<String> retry(Observable<String> data) {
        //return Observable.error(new RuntimeException("Not Implemented"));
        return data.onErrorResumeNext(Observable.just("default-value"));

    }

    // This function can be used to build JSON objects within an expression
    private static JSON json(Object... keyOrValue) {
        JSON json = new JSON();

        for (int counter = 0; counter < keyOrValue.length; counter += 2) {
            json.put((String) keyOrValue[counter], keyOrValue[counter + 1]);
        }

        return json;
    }
}
