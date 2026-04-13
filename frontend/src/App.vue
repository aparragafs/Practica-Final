<template>
  <div>
    <header class="topbar">
      <div class="brand">
        <img src="/logo.png" alt="Logo empresa" class="logo" />

        <div class="brand-text">
          <h1>Intranet RRHH</h1>
          <span>Gestión corporativa</span>
        </div>
      </div>

      <nav class="nav-links">
        <RouterLink to="/empleados">Empleados</RouterLink>
        <RouterLink to="/proyectos">Proyectos</RouterLink>
        <RouterLink to="/asignaciones">Asignaciones</RouterLink>
      </nav>

      <div class="user-area">
        <span>Administrador</span>
      </div>
    </header>

    <main>
      <RouterView />
    </main>
  </div>
</template>

<style>
.topbar {
  height: 78px;
  background: linear-gradient(90deg, #1e293b, #0f172a);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 36px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.12);
}

.brand {
  display: flex;
  align-items: center;
  gap: 14px;
  min-width: 260px;
}

.logo {
  width: 46px;
  height: 46px;
  object-fit: contain;
  border-radius: 10px;
  background: white;
  padding: 4px;
}

.brand-text {
  display: flex;
  flex-direction: column;
}

.brand-text h1 {
  margin: 0;
  color: white;
  font-size: 20px;
  font-weight: 700;
}

.brand-text span {
  color: #94a3b8;
  font-size: 13px;
}

.nav-links {
  display: flex;
  gap: 32px;
}

.nav-links a {
  position: relative;
  color: #cbd5e1;
  text-decoration: none;
  font-size: 15px;
  font-weight: 600;
  padding: 8px 0;
  transition: all 0.2s ease;
}

.nav-links a:hover {
  color: white;
}

.nav-links a.router-link-active {
  color: white;
}

.nav-links a.router-link-active::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: -6px;
  width: 100%;
  height: 3px;
  border-radius: 10px;
  background: #3b82f6;
}

.user-area {
  min-width: 140px;
  text-align: right;
  color: #cbd5e1;
  font-size: 14px;
  font-weight: 500;
}
</style>