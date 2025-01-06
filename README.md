# Web crawler

## Table of Contents
### After revisiting this repo in 1 year, I've realized that I'm a dumbass. Program stores all data in redis and only last results in db, meaning that the crash is inevitable because we will run out of RAM

- [Features](#features)
- [Prerequisites](#prerequisites)
  
## Features

- Multithread, each crawler instanse is a thread which works with specific frontier queue
- Redis storage for frontier, hots data and visited urls
- SEO data stores in a specific file in project folder
- Fast data processing by regex
- You can configure:
  - Time between fething
  - Max number of crawlers
  - Save file location

## Prerequisites

You should have installed redis-server which will be running on port 6379
