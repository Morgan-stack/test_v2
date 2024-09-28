-- Add a unique constraint on the email column
ALTER TABLE customer
ADD CONSTRAINT customer_email_unique UNIQUE (email);