FROM postgres:16

# Set environment variables for PostgreSQL
ENV POSTGRES_DB=hms
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=hms2025

# Copy SQL schema to the Docker image's init directory
COPY database.sql /docker-entrypoint-initdb.d/
