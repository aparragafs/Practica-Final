from sqlalchemy import create_engine

usuario = "root"
password = "root"
host = "localhost"
puerto = 3306
bbdd = "PRACTICA"

engine = create_engine(
    f"mysql+pymysql://{usuario}:{password}@{host}:{puerto}/{bbdd}"
)