-- Atualizando a senha do admin pois o hash na V3 estava incorreto
UPDATE usuarios SET senha = '$2a$10$uLPKWWMeNKUz69wFw2VLA.NTdTIUUw2LURbxI4gOwXcXF0boEtMay' WHERE email = 'admin@restall.com.br';
