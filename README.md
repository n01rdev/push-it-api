<a name="readme-top"></a>

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/n01rdev/push-it-api/">
    <img src="readme/images/logo.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Push It API</h3>

  <p align="center">
    A robust and secure API built with Kotlin and Spring Boot.
    <br />
    <a href="https://github.com/n01rdev/push-it-api"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/n01rdev/push-it-api/issues">Report Bug</a>
    ·
    <a href="https://github.com/n01rdev/push-it-api/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

Push It API is a robust and secure API built with Kotlin and Spring Boot. It provides a solid foundation for building complex web applications.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

[![Spring Boot][Spring]][Spring-url]

[![Kotlin][Kotlin]][Kotlin-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple example steps.

### Prerequisites

* Docker
* Make (optional but recommended)

### Installation

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- USAGE EXAMPLES -->
## Usage

The Push It API provides a robust and secure backend for web applications. Here are some examples of how you can use it:

### User Registration

To register a new user, send a `POST` request to `/api/v1/security/register` with the user's email and password in the request body:

```sh
curl -X POST -H "Content-Type: application/json" -d '{"email": "user@example.com", "password": "password"}' http://localhost:8080/api/v1/security/register
```

The API will return a JWE token which can be used for user authentication in subsequent requests.

### User Authentication

To authenticate a user, include the JWE token in the Authorization header of your requests:

```sh
curl -H "Authorization: Bearer <JWE_TOKEN>" http://localhost:8080/api/v1/protected-endpoint
```

Replace <JWE_TOKEN> with the token you received during user registration.

### Error Handling

The API uses standard HTTP status codes to indicate the success or failure of a request. In case of an error, the response will include a message providing more details about the error. 

For example, if you try to register a user with an email that is already in use, the API will return a 409 Conflict status code and a message indicating that the email is already in use.

_For more examples, please refer to the [Documentation](https://example.com)_

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ROADMAP -->
## Roadmap

- [ ] Feature 1
- [ ] Feature 2
- [ ] Feature 3
    - [ ] Nested Feature

See the [open issues](https://github.com/github_username/repo_name/issues) for a full list of proposed features (and known issues).

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Your Name - [@twitter_handle](https://twitter.com/twitter_handle) - email@email_client.com

Project Link: [https://github.com/github_username/repo_name](https://github.com/github_username/repo_name)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* []()
* []()
* []()

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/n01rdev/push-it-api.svg?style=for-the-badge&color=black&labelColor=white
[contributors-url]: https://github.com/n01rdev/push-it-api/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/n01rdev/push-it-api.svg?style=for-the-badge&color=black&labelColor=white
[forks-url]: https://github.com/n01rdev/push-it-api/network/members
[stars-shield]: https://img.shields.io/github/stars/n01rdev/push-it-api.svg?style=for-the-badge&color=black&labelColor=white
[stars-url]: https://github.com/n01rdev/push-it-api/stargazers
[issues-shield]: https://img.shields.io/github/issues/n01rdev/push-it-api.svg?style=for-the-badge&color=black&labelColor=white
[issues-url]: https://github.com/n01rdev/push-it-api/issues
[license-shield]: https://img.shields.io/github/license/n01rdev/push-it-api.svg?style=for-the-badge&color=black&labelColor=white
[license-url]: https://github.com/n01rdev/push-it-api/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=black&logoColor=white
[linkedin-url]: https://www.linkedin.com/in/azdev/
[product-screenshot]: readme/images/screenshot.png
[Vue.js]: https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D
[Vue-url]: https://vuejs.org/
[Spring]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white&color=black
[Spring-url]: https://spring.io/
[Kotlin]: https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white&color=black
[Kotlin-url]: https://kotlinlang.org/