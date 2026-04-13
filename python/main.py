import pandas as pd
import conexion

empleados = pd.read_sql(
    "SELECT * FROM EM_EMPLEADOS",
    conexion.engine
)

print(len(empleados))