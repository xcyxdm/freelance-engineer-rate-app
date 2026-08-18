CREATE TABLE interview_reports (
    id BIGSERIAL PRIMARY KEY,
    company VARCHAR(120) NOT NULL,
    age INTEGER NOT NULL,
    role VARCHAR(80) NOT NULL,
    unit_price INTEGER NOT NULL,
    project_summary TEXT NOT NULL,
    result VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO interview_reports (
    company,
    age,
    role,
    unit_price,
    project_summary,
    result
) VALUES (
    '○○',
    48,
    'Java',
    75,
    'Spring Boot経験
AWS経験
設計経験',
    'HIRED'
);
