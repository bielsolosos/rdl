TRUNCATE TABLE urls_redirect CASCADE;

ALTER TABLE urls_redirect 
ADD COLUMN user_id BIGINT NOT NULL;

ALTER TABLE urls_redirect 
ADD CONSTRAINT fk_urls_redirect_user 
FOREIGN KEY (user_id) 
REFERENCES users(id) 
ON DELETE CASCADE;

-- Cria índice para melhor performance nas consultas por usuário
CREATE INDEX idx_user_id ON urls_redirect(user_id);
