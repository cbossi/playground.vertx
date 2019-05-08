FROM postgres:10.7

# If we would NOT need a schema, we could make use of the environment variables instead:
# https://stackoverflow.com/a/26599273/6231489
COPY ./init.sql /docker-entrypoint-initdb.d/
