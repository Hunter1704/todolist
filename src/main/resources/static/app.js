const apiBase = "/api/tasks";

const form = document.getElementById("task-form");
const nameInput = document.getElementById("task-name");
const descInput = document.getElementById("task-desc");
const statusInput = document.getElementById("task-status");
const submitBtn = document.getElementById("submit-btn");
const cancelBtn = document.getElementById("cancel-btn");
const messageEl = document.getElementById("form-message");
const listEl = document.getElementById("task-list");
const emptyEl = document.getElementById("empty-state");
const filtersEl = document.getElementById("filters");

const statTotal = document.getElementById("stat-total");
const statPending = document.getElementById("stat-pending");
const statProgress = document.getElementById("stat-progress");
const statCompleted = document.getElementById("stat-completed");

const statusLabels = {
  PENDING: "Pending",
  IN_PROGRESS: "In progress",
  COMPLETED: "Completed",
};

let tasks = [];
let activeFilter = "ALL";
let editingId = null;

const setMessage = (text) => {
  messageEl.textContent = text || "";
};

const setFormMode = (task) => {
  if (task) {
    editingId = task.id;
    nameInput.value = task.name || "";
    descInput.value = task.description || "";
    statusInput.value = task.status || "PENDING";
    submitBtn.textContent = "Update task";
    cancelBtn.hidden = false;
  } else {
    editingId = null;
    form.reset();
    statusInput.value = "PENDING";
    submitBtn.textContent = "Add task";
    cancelBtn.hidden = true;
  }
  setMessage("");
};

const updateStats = () => {
  const counts = tasks.reduce(
    (acc, task) => {
      acc.total += 1;
      acc[task.status] += 1;
      return acc;
    },
    { total: 0, PENDING: 0, IN_PROGRESS: 0, COMPLETED: 0 }
  );
  statTotal.textContent = counts.total;
  statPending.textContent = counts.PENDING;
  statProgress.textContent = counts.IN_PROGRESS;
  statCompleted.textContent = counts.COMPLETED;
};

const statusClass = (status) => {
  if (status === "IN_PROGRESS") return "progress";
  if (status === "COMPLETED") return "completed";
  return "pending";
};

const renderTasks = () => {
  listEl.innerHTML = "";
  const filtered =
    activeFilter === "ALL"
      ? tasks
      : tasks.filter((task) => task.status === activeFilter);

  emptyEl.hidden = filtered.length !== 0;

  filtered.forEach((task) => {
    const card = document.createElement("article");
    card.className = "task-card";
    card.dataset.id = task.id;

    const title = document.createElement("div");
    title.className = "task-title";
    title.textContent = task.name;

    const desc = document.createElement("p");
    desc.className = "task-desc";
    desc.textContent = task.description;

    const meta = document.createElement("div");
    meta.className = "task-meta";

    const pill = document.createElement("span");
    pill.className = `status-pill ${statusClass(task.status)}`;
    pill.textContent = statusLabels[task.status] || task.status;
    meta.appendChild(pill);

    const actions = document.createElement("div");
    actions.className = "task-actions";

    const editBtn = document.createElement("button");
    editBtn.type = "button";
    editBtn.dataset.action = "edit";
    editBtn.textContent = "Edit";

    const deleteBtn = document.createElement("button");
    deleteBtn.type = "button";
    deleteBtn.dataset.action = "delete";
    deleteBtn.textContent = "Delete";

    actions.appendChild(editBtn);
    actions.appendChild(deleteBtn);

    if (task.status !== "COMPLETED") {
      const doneBtn = document.createElement("button");
      doneBtn.type = "button";
      doneBtn.dataset.action = "complete";
      doneBtn.className = "primary";
      doneBtn.textContent = "Mark done";
      actions.appendChild(doneBtn);
    }

    card.appendChild(title);
    card.appendChild(desc);
    card.appendChild(meta);
    card.appendChild(actions);
    listEl.appendChild(card);
  });
};

const fetchTasks = async () => {
  try {
    const res = await fetch(apiBase);
    if (!res.ok) {
      throw new Error("Failed to fetch tasks.");
    }
    tasks = await res.json();
    updateStats();
    renderTasks();
  } catch (err) {
    setMessage(err.message || "Unable to load tasks.");
  }
};

const saveTask = async (payload) => {
  const url = editingId ? `${apiBase}/${editingId}` : apiBase;
  const method = editingId ? "PUT" : "POST";
  const res = await fetch(url, {
    method,
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (!res.ok) {
    throw new Error("Unable to save task.");
  }
  return res.json();
};

const updateTaskStatus = async (task, status) => {
  const res = await fetch(`${apiBase}/${task.id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      name: task.name,
      description: task.description,
      status,
    }),
  });

  if (!res.ok) {
    throw new Error("Unable to update task.");
  }
};

const deleteTask = async (id) => {
  const res = await fetch(`${apiBase}/${id}`, { method: "DELETE" });
  if (!res.ok) {
    throw new Error("Unable to delete task.");
  }
};

form.addEventListener("submit", async (event) => {
  event.preventDefault();
  const payload = {
    name: nameInput.value.trim(),
    description: descInput.value.trim(),
    status: statusInput.value,
  };

  if (!payload.name || !payload.description) {
    setMessage("Name and description are required.");
    return;
  }

  submitBtn.disabled = true;
  setMessage("");
  try {
    await saveTask(payload);
    await fetchTasks();
    setFormMode(null);
  } catch (err) {
    setMessage(err.message || "Could not save task.");
  } finally {
    submitBtn.disabled = false;
  }
});

cancelBtn.addEventListener("click", () => setFormMode(null));

filtersEl.addEventListener("click", (event) => {
  const button = event.target.closest("button");
  if (!button) return;
  activeFilter = button.dataset.filter;
  filtersEl.querySelectorAll("button").forEach((btn) => {
    btn.classList.toggle("active", btn === button);
  });
  renderTasks();
});

listEl.addEventListener("click", async (event) => {
  const actionBtn = event.target.closest("button[data-action]");
  if (!actionBtn) return;

  const card = actionBtn.closest(".task-card");
  const task = tasks.find((item) => item.id === card.dataset.id);
  if (!task) return;

  try {
    if (actionBtn.dataset.action === "edit") {
      setFormMode(task);
      return;
    }

    if (actionBtn.dataset.action === "delete") {
      await deleteTask(task.id);
    }

    if (actionBtn.dataset.action === "complete") {
      await updateTaskStatus(task, "COMPLETED");
    }

    await fetchTasks();
  } catch (err) {
    setMessage(err.message || "Action failed.");
  }
});

fetchTasks();
