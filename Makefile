docker-up:
	@echo "Up All Services"
	docker-compose up -d

docker-down:
	@echo "Down docker-compose"
	docker-compose down

docker-clear-all:
	@echo "Warning !!!! Delete ALL volumes, containers and images"
	docker volume prune
	docker system prune -a

docker-access-db:
	@echo "Access to container DB"
	docker exec -ti db bash

docker-access-app:
	@echo "Access to container App"
	docker exec -ti app bash

docker-app-lint:
	@echo "Lint Kotlin"
	docker exec app ./gradlew ktlintCheck