# Microblog:

This REST Spring service is configured to use Elasticsearch as a @Repository.

In order to run the service, download and extract elasticsearch
Tested to work with ES 6.2.4, but any version should work.

Use postman collection file to test and use the functionality.

Endpoints supported:

*GET /post*

*POST /post/*

*PATCH /post/{id}*

*DELETE /post/{id}*

*POST /post/{id}/upvote*

*POST /post/{id}/downvote*

*GET /post/top_posts*

example usage:

1. Create a new post:
```
POST /post/
{
	"text": "one fish two fish blue fish red fish"
}

Response
{
    "id": "601abace-b8e9-479c-9ea1-857fecab1636",
    "text": "one fish two fish blue fish red fish",
    "rating": 0,
    "votes": {}
}
```
2. upvote a post:
```
POST /post/601abace-b8e9-479c-9ea1-857fecab1636/upvote
{
	"userId": "1"
}

Response:
{
    "id": "601abace-b8e9-479c-9ea1-857fecab1636",
    "text": "one fish two fish blue fish red fish",
    "rating": 4,
    "votes": {
        "1": 1,
        "2": 1,
        "3": 1
    }
}
```
