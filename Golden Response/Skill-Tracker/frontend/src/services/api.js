const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

async function request(path, options = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...(options.headers ?? {})
  };

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers
  });

  let payload = null;
  const contentType = response.headers.get("content-type") || "";
  if (contentType.includes("application/json")) {
    payload = await response.json();
  }

  if (!response.ok) {
    throw new Error(payload?.message || `Request failed with status ${response.status}`);
  }

  return payload;
}

function authHeaders(token) {
  return token ? { Authorization: `Bearer ${token}` } : {};
}

export function login(credentials) {
  return request("/auth/login", {
    method: "POST",
    body: JSON.stringify(credentials)
  });
}

export function register(payload) {
  return request("/auth/register", {
    method: "POST",
    body: JSON.stringify(payload)
  });
}

export function getProfile(token) {
  return request("/auth/me", {
    headers: authHeaders(token)
  });
}

export function getDashboard(token) {
  return request("/dashboard", {
    headers: authHeaders(token)
  });
}

export function getSkills(token) {
  return request("/skills", {
    headers: authHeaders(token)
  });
}

export function createSkill(token, payload) {
  return request("/skills", {
    method: "POST",
    headers: authHeaders(token),
    body: JSON.stringify(payload)
  });
}

export function updateSkill(token, skillId, payload) {
  return request(`/skills/${skillId}`, {
    method: "PUT",
    headers: authHeaders(token),
    body: JSON.stringify(payload)
  });
}

export function deleteSkill(token, skillId) {
  return request(`/skills/${skillId}`, {
    method: "DELETE",
    headers: authHeaders(token)
  });
}

export function getGoals(token) {
  return request("/goals", {
    headers: authHeaders(token)
  });
}

export function createGoal(token, payload) {
  return request("/goals", {
    method: "POST",
    headers: authHeaders(token),
    body: JSON.stringify(payload)
  });
}

export function updateGoal(token, goalId, payload) {
  return request(`/goals/${goalId}`, {
    method: "PUT",
    headers: authHeaders(token),
    body: JSON.stringify(payload)
  });
}

export function deleteGoal(token, goalId) {
  return request(`/goals/${goalId}`, {
    method: "DELETE",
    headers: authHeaders(token)
  });
}

export function getAssessments(token) {
  return request("/assessments", {
    headers: authHeaders(token)
  });
}

export function submitAssessment(token, assessmentId, answers) {
  return request(`/assessments/${assessmentId}/submit`, {
    method: "POST",
    headers: authHeaders(token),
    body: JSON.stringify({ answers })
  });
}

export function getTraining(token) {
  return request("/training", {
    headers: authHeaders(token)
  });
}

export function getRoadmaps(token) {
  return request("/roadmaps", {
    headers: authHeaders(token)
  });
}

export function createRoadmap(token, payload) {
  return request("/roadmaps", {
    method: "POST",
    headers: authHeaders(token),
    body: JSON.stringify(payload)
  });
}

export function updateRoadmap(token, roadmapId, payload) {
  return request(`/roadmaps/${roadmapId}`, {
    method: "PUT",
    headers: authHeaders(token),
    body: JSON.stringify(payload)
  });
}

export function deleteRoadmap(token, roadmapId) {
  return request(`/roadmaps/${roadmapId}`, {
    method: "DELETE",
    headers: authHeaders(token)
  });
}

export function toggleRoadmapStep(token, roadmapId, stepId) {
  return request(`/roadmaps/${roadmapId}/steps/${stepId}/toggle`, {
    method: "PATCH",
    headers: authHeaders(token)
  });
}
