FROM neowaylabs/caddy:0.8.3

COPY client/ app/

CMD ["/caddy", "-root=/app", "-http2=false", "-port=8080"]
