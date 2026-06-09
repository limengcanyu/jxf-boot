INSERT INTO documents (id, filename, original_filename, file_type, file_size, content, created_at) VALUES ('doc-001', '企业AI白皮书.pdf', '企业AI白皮书.pdf', 'application/pdf', 1024000, '人工智能白皮书内容...', '2024-01-15 10:30:00');
INSERT INTO documents (id, filename, original_filename, file_type, file_size, content, created_at) VALUES ('doc-002', '产品设计文档.docx', '产品设计文档.docx', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', 512000, '产品设计文档内容...', '2024-01-16 14:20:00');
INSERT INTO documents (id, filename, original_filename, file_type, file_size, content, created_at) VALUES ('doc-003', '数据分析报告.xlsx', '数据分析报告.xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 256000, '数据分析报告内容...', '2024-01-17 09:15:00');
INSERT INTO documents (id, filename, original_filename, file_type, file_size, content, created_at) VALUES ('doc-004', '技术架构说明.txt', '技术架构说明.txt', 'text/plain', 10240, '技术架构说明内容...', '2024-01-18 16:45:00');
INSERT INTO documents (id, filename, original_filename, file_type, file_size, content, created_at) VALUES ('doc-005', '项目进度计划.pptx', '项目进度计划.pptx', 'application/vnd.openxmlformats-officedocument.presentationml.presentation', 768000, '项目进度计划内容...', '2024-01-19 11:00:00');

INSERT INTO document_chunks (id, document_id, chunk_index, content) VALUES ('chunk-001', 'doc-001', 1, '人工智能（AI）正在改变企业运营方式。本白皮书介绍了AI在企业中的应用场景和最佳实践。');
INSERT INTO document_chunks (id, document_id, chunk_index, content) VALUES ('chunk-002', 'doc-001', 2, '机器学习是AI的核心技术之一，包括监督学习、无监督学习和强化学习三种主要类型。');
INSERT INTO document_chunks (id, document_id, chunk_index, content) VALUES ('chunk-003', 'doc-001', 3, '自然语言处理（NLP）技术使计算机能够理解和处理人类语言，包括文本分类、情感分析和机器翻译。');
INSERT INTO document_chunks (id, document_id, chunk_index, content) VALUES ('chunk-004', 'doc-002', 1, '产品设计原则包括用户中心设计、可用性、一致性和可扩展性。');
INSERT INTO document_chunks (id, document_id, chunk_index, content) VALUES ('chunk-005', 'doc-002', 2, '用户体验（UX）设计流程包括研究、设计、测试和迭代四个阶段。');
INSERT INTO document_chunks (id, document_id, chunk_index, content) VALUES ('chunk-006', 'doc-003', 1, '数据分析方法包括描述性分析、诊断性分析、预测性分析和规范性分析。');
INSERT INTO document_chunks (id, document_id, chunk_index, content) VALUES ('chunk-007', 'doc-003', 2, '数据可视化工具可以帮助用户更好地理解和分析数据，常见的有图表、仪表盘和报告。');
INSERT INTO document_chunks (id, document_id, chunk_index, content) VALUES ('chunk-008', 'doc-004', 1, '微服务架构将应用程序分解为小型、独立的服务，每个服务运行在自己的进程中。');
INSERT INTO document_chunks (id, document_id, chunk_index, content) VALUES ('chunk-009', 'doc-004', 2, '容器化技术（如Docker）可以简化应用部署和管理，提高开发和运维效率。');
INSERT INTO document_chunks (id, document_id, chunk_index, content) VALUES ('chunk-010', 'doc-005', 1, '项目管理方法论包括敏捷、Scrum、看板和瀑布模型，各有其适用场景。');