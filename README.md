# Web crawler

## Table of Contents

- [About](#about)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- 
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
