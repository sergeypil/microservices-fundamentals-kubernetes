INSERT INTO storage_object (id, type, bucket, path) VALUES (1, 'STAGING', 'staging-bucket', '/files') 
ON CONFLICT (id) DO NOTHING;
INSERT INTO storage_object (id, type, bucket, path) VALUES (2, 'PERMANENT', 'permanent-bucket', '/files')
ON CONFLICT (id) DO NOTHING;