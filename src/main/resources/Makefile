all: drop_db migrate
migrate:
	  @echo "Migrating..."
	  @sqlite3 prod.db < migrations/create_tables.sql

drop_db:
	  @echo "Dropping"
	  @sqlite3 prod.db < migrations/drop_db.sql

# Clean the binary
clean: drop_db
	  @echo "Cleaning..."
	  @rm -f main

.PHONY: all migrate drop_db