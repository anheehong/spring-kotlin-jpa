CREATE TABLE TB_USER (
                            USER_NAME           varchar(50) NOT NULL,
                            USER_PASSWORD       varchar(255) NOT NULL,
                            DISPLAY_NAME        varchar(50) NOT NULL,
                            TOKEN               varchar(1000) NULL,
                            DT_CREATE           timestamp DEFAULT CURRENT_DATE NOT NULL,
                            DT_UPDATE           timestamp DEFAULT CURRENT_DATE,
                            DT_LAST_LOGIN       timestamp,
                            PRIMARY KEY (USER_NAME)
                  );