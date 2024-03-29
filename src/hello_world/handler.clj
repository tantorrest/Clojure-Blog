(ns hello-world.handler
  (:use [compojure.core]
	[hiccup.core]
  )
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
	        [taoensso.carmine :as car :refer (wcar)]
            [hiccup.form :as form]
))


; Approach:

; 1.    
; Learn basic redis in the command line. There are tutorials for this. Pay special attention to building lists of things and using the "append" command
; Activate redis with redis-cli in terminal

; 2. Try saving blog posts to disk. Line 44

; 3. Show the list of blog posts instead of using the for loop. Line 31


(def redis-conn {:pool {} :spec {}})
(defmacro wcar* [& body] `(car/wcar redis-conn ~@body))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/blog" [] 
	(html 
	    [:ul
	        ; TODO: Replace this for loop with REAL blog posts that have been retrieved from the database.
            ;(let [post-list (wcar* (car/get "post-list"))]
            (for [x (range (wcar* (car/llen "post-list")))]
            [:li (wcar* (car/lindex "post-list" x))])]
            
            ;(for [x (range 1 10)]
            
            ;[:li "Blah blah an old blog post"])]
            (form/form-to [:post "/blog"]
        ;(form/form-to [:post "http://requestb.in/pfk0eopf"]
    	    (form/text-area "blog-post" "My new post goes here")
    	    (form/submit-button "Post"))
     )
  )
  
  
  ;(POST "/blog"  {{blog-post :blog-post} :params}        
  (POST "/blog"  {params :params}        
      (let [blog-post (:blog-post params)]
      (wcar* (car/lpush "post-list" blog-post))
      ; TODO: Save the new "blog post" to the database
     ;   (html
     ;       [:p "You just wrote the following novel:"]
     ;       [:p blog-post]
     ;   )
       (ring.util.response/redirect "/blog")
        
        ))
  
  (GET "/counter" [] 
	; increment the redis counter by one
	; show the current counter value to the user
	(let [db-results	(wcar*  (car/incr "visitors"))]
		(str "Visitors so far: " db-results)
    )
  )

  (route/resources "/media")
  (route/not-found "Not Found")

)

(def app
  (handler/site app-routes))
