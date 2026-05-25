<p align="center">
  <img src="./src/main/resources/static/images/logo.png" alt="ShopRecommendation logo" width="170">
</p>

<h1 align="center">ShopRecommendation</h1>

<p align="center">A Java Web e-commerce recommendation system covering product browsing, category search, favorites, cart, orders, Alipay sandbox flow, and recommended products.</p>

<p align="center">
  <a href="./README.md">简体中文</a> | <a href="./README.en.md">English</a>
</p>

<p align="center">
  <img alt="Status" src="https://img.shields.io/badge/status-portfolio-7952B3?style=for-the-badge">
  <img alt="Stack" src="https://img.shields.io/badge/stack-Spring%20Boot%20%2B%20Thymeleaf%20%2B%20JPA-2E7D32?style=for-the-badge">
  <img alt="Screenshot" src="https://img.shields.io/badge/screenshot-static%20template-F59E0B?style=for-the-badge">
  <a href="./LICENSE"><img alt="License" src="https://img.shields.io/badge/license-Apache--2.0-blue?style=for-the-badge"></a>
</p>

<p align="center">
  <img src="./docs/assets/screenshots/static-template-preview.png" alt="ShopRecommendation static template preview screenshot" width="900">
</p>

ShopRecommendation is a Java Web course-stage project organized as a public portfolio repository. It includes Spring Boot backend code, Thymeleaf page templates, static page assets, JPA entities, and service layering. Local database URLs, database passwords, and Alipay sandbox keys have been converted to environment-based configuration.

The repository does not include the historical local database or real payment keys. A complete runtime demo requires a prepared MySQL database with sample products, categories, users, and orders.

## Features

- User registration, login, and logout.
- Home page product display, category browsing, and product search.
- Product details, price, stock, sales count, and view count display.
- Favorites, favorites list, cart add / remove / quantity update, and checkout.
- Shipping address management, order creation, order list, and payment callback flow.
- Alipay sandbox payment parameter integration.
- Recommended-product model and homepage recommendation display.

## Tech Stack

| Area | Stack |
| --- | --- |
| Backend | Spring Boot 2.5.0, Spring MVC, Spring Data JPA, Lombok |
| Pages | Thymeleaf, HTML, CSS, Bootstrap, jQuery |
| Database | MySQL, Hibernate / JPA |
| Payment | Alipay SDK, Alipay sandbox |
| API docs | Springfox Swagger 2 |
| Build tools | Maven Wrapper |

## Local Development

Static template preview can be served without the backend:

```powershell
cd D:\code\ShopRecommendation\src\main\resources\static
python -m http.server 9898
```

For full backend execution, create a MySQL database and provide the required environment variables documented in the Chinese README and `.env.example`.

## License and Security

This repository uses the Apache License 2.0. See [LICENSE](LICENSE).

Security reporting instructions are in [SECURITY.md](SECURITY.md), and contribution notes are in [CONTRIBUTING.md](CONTRIBUTING.md).
