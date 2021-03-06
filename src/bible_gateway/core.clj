(ns bible-gateway.core
  (:require [net.cgrand.enlive-html :as html]))

;; Defaults
(def version "NRSVCE")

(defn search-string [book chapter verse]
  (str book "+" chapter
       (when verse (str "%3A" verse))))

(defn bible-gateway-url [search-string version]
  (str "https://classic.biblegateway.com/passage/?search=" search-string "&version=" version "&interface=print"))

(defn url->quote [url]
  (->> (java.net.URL. url)
       html/html-resource
       (#(html/select % [:meta]))
       (map :attrs)
       (filter #(= "og:description" (:property %)))
       first
       :content))

(defn bible-quote
  ([book chapter version]
   (bible-quote book chapter nil version))
  ([book chapter verse version]
   (-> (search-string book chapter verse)
       (bible-gateway-url version)
       url->quote)))

(comment
  (url->quote "https://www.biblegateway.com/passage/?search=john+3%3A16&version=NRSVCE&interface=print")

  (bible-quote "john" 3 16 version)
  )
