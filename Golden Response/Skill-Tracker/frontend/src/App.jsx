import { useEffect, useState } from "react";
import {
  createGoal,
  createRoadmap,
  createSkill,
  deleteGoal,
  deleteRoadmap,
  deleteSkill,
  getAssessments,
  getDashboard,
  getGoals,
  getProfile,
  getRoadmaps,
  getSkills,
  getTraining,
  login,
  register,
  submitAssessment,
  toggleRoadmapStep,
  updateGoal,
  updateRoadmap,
  updateSkill
} from "./services/api";

const initialSkillForm = {
  name: "",
  category: "Backend",
  proficiencyLevel: "BEGINNER",
  confidenceScore: 50,
  targetScore: 80,
  priorityRank: 3,
  lastPracticedOn: "",
  learningNote: ""
};

const initialGoalForm = {
  skillId: "",
  title: "",
  description: "",
  targetDate: "",
  status: "PLANNED"
};

function formatShortDate(value) {
  if (!value) {
    return "No date";
  }

  return new Intl.DateTimeFormat("en-US", {
    month: "short",
    day: "numeric"
  }).format(new Date(value));
}

function formatLongDate(value) {
  if (!value) {
    return "No recent activity";
  }

  return new Intl.DateTimeFormat("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric"
  }).format(new Date(value));
}

function daysUntil(targetDate) {
  if (!targetDate) {
    return null;
  }

  const today = new Date();
  const target = new Date(targetDate);
  const difference = target.setHours(0, 0, 0, 0) - today.setHours(0, 0, 0, 0);
  return Math.ceil(difference / (1000 * 60 * 60 * 24));
}

function progressPercentage(current, total) {
  if (!total) {
    return 0;
  }

  return Math.max(0, Math.min(100, Math.round((current / total) * 100)));
}

const initialRoadmapForm = {
  title: "",
  goal: "",
  currentLevel: "Beginner",
  targetLevel: "Advanced"
};

function StatCard({ label, value, helper, tone }) {
  return (
    <article className={`stat-card stat-card--${tone}`}>
      <span className="stat-card__label">{label}</span>
      <strong className="stat-card__value">{value}</strong>
      <span className="stat-card__helper">{helper}</span>
    </article>
  );
}
function SectionHeader({ title, description, action }) {
  return (
    <div className="section-header">
      <div>
        <p className="section-kicker">{title}</p>
        <h2>{description}</h2>
      </div>
      {action}
    </div>
  );
}

function AuthPanel({ mode, form, onChange, onSubmit, onSwitch, loading, error }) {
  return (
    <section className="auth-card">
      <div className="auth-card__copy">
         <span className="eyebrow">Skill Tracker Pro</span>
         <h1>Build a visible learning streak, not just a list of skills.</h1>
        <p>
        Track your strongest focus areas, turn practice into momentum, and keep your growth system
                  moving with assessments, goals, and training in one place.
        </p>
        <div className="auth-card__highlights">
           <span>Streak dashboard</span>
                    <span>Assessment feedback</span>
                    <span>Goal-driven practice</span>
                  </div>
                  <div className="auth-card__spotlight">
                    <strong>Daily consistency matters</strong>
                    <p>
                      The new dashboard keeps your weekly rhythm visible, so you always know whether today is
                      extending momentum or restarting it.
                    </p>
        </div>
        <p className="demo-hint">
          Demo account: <strong>demo@skilltracker.dev</strong> / <strong>password123</strong>
        </p>
      </div>

      <form className="auth-form" onSubmit={onSubmit}>
          <span className="eyebrow eyebrow--soft">{mode === "login" ? "Sign in" : "Register"}</span>
        <h2>{mode === "login" ? "Welcome back" : "Create your account"}</h2>
        <p className="muted-copy">Use your workspace to add skills, set goals, and keep the streak alive.</p>

        {mode === "register" ? (
          <label>
            Full name
            <input name="fullName" value={form.fullName || ""} onChange={onChange} required />
          </label>
        ) : null}
        <label>
          Email
          <input name="email" type="email" value={form.email} onChange={onChange} required />
        </label>
        <label>
          Password
          <input name="password" type="password" value={form.password} onChange={onChange} required />
        </label>
        {error ? <p className="form-error">{error}</p> : null}
        <button className="primary-button" type="submit" disabled={loading}>
            {loading ? "Working..." : mode === "login" ? "Enter workspace" : "Create workspace"}
        </button>
        <button className="secondary-button" type="button" onClick={onSwitch}>
          {mode === "login" ? "Need an account? Register" : "Already have an account? Sign in"}
        </button>
      </form>
    </section>
  );
}


function GrowthPanel({ dailyGrowth }) {
      const weeklyProgress = progressPercentage(dailyGrowth.activeDaysThisWeek, dailyGrowth.weeklyTarget);

      return (
        <section className="panel panel--growth">
          <SectionHeader
            title="Daily growth"
            description="Your streak, weekly activity, and near-term momentum."
            action={<span className={`status-pill ${dailyGrowth.practicedToday ? "status-pill--active" : ""}`}>{dailyGrowth.practicedToday ? "Practiced today" : "Not active today"}</span>}
          />

          <div className="growth-grid">
            <article className="growth-hero-card">
              <div>
                <span className="eyebrow eyebrow--soft">Current streak</span>
                <div className="streak-metric">
                  <strong>{dailyGrowth.currentStreak}</strong>
                  <span>days</span>
                </div>
                <p>{dailyGrowth.momentumLabel}</p>
              </div>
              <div className="streak-summary">
                <div>
                  <span>Longest streak</span>
                  <strong>{dailyGrowth.longestStreak} days</strong>
                </div>
                <div>
                  <span>Weekly target</span>
                  <strong>
                    {dailyGrowth.activeDaysThisWeek}/{dailyGrowth.weeklyTarget} days
                  </strong>
                </div>
              </div>
              <div className="progress-track" aria-hidden="true">
                <span style={{ width: `${weeklyProgress}%` }} />
              </div>
              <small>{dailyGrowth.nextMilestone}</small>
            </article>

            <article className="growth-heatmap-card">
              <div className="growth-heatmap-card__header">
                <div>
                  <span className="eyebrow eyebrow--soft">Recent activity</span>
                  <h3>Last 14 days</h3>
                </div>
                <strong>{weeklyProgress}% weekly pace</strong>
              </div>
              <div className="heatmap-grid">
                {dailyGrowth.recentActivity.map((entry) => (
                  <div className="heatmap-cell-wrap" key={entry.date}>
                    <span className={`heatmap-cell ${entry.active ? "heatmap-cell--active" : ""}`} title={formatLongDate(entry.date)} />
                    <small>{formatShortDate(entry.date)}</small>
                  </div>
                ))}
              </div>
            </article>
          </div>
        </section>
      );
    }

function FocusSkillsPanel({ skills }) {
  return (
     <section className="panel">
          <SectionHeader title="Focus skills" description="The most important areas to push forward next." />
          <div className="focus-skill-list">
            {skills.length === 0 ? (
              <div className="empty-state">
                <strong>No skills yet</strong>
                <p>Add your first skill to unlock focus suggestions and progress pacing.</p>
              </div>
            ) : (
              skills.map((skill) => {
                const completion = progressPercentage(skill.confidenceScore, skill.targetScore);
                return (
                  <article className="focus-skill-card" key={skill.id}>
                    <div className="focus-skill-card__top">
                      <div>
                        <span className="chip">{skill.category}</span>
                        <h3>{skill.name}</h3>
                      </div>
                      <span className="focus-skill-card__level">{skill.proficiencyLevel}</span>
                    </div>
                    <div className="focus-skill-card__progress">
                      <div>
                        <strong>{skill.confidenceScore}%</strong>
                        <span>confidence</span>
                      </div>
                      <div>
                        <strong>{skill.targetScore}%</strong>
                        <span>target</span>
                      </div>
                      <div>
                        <strong>P{skill.priorityRank}</strong>
                        <span>priority</span>
                      </div>
                    </div>
                    <div className="progress-track" aria-hidden="true">
                      <span style={{ width: `${completion}%` }} />
                    </div>
                    <p>{skill.learningNote || "Add a learning note to keep practice tied to a clear objective."}</p>
                  </article>
                );
              })
            )}
      </div>
      </section>
  );
}

function SkillForm({ form, editingId, onChange, onSubmit, onCancel, loading }) {
  return (
    <form className="panel panel--form" onSubmit={onSubmit}>
        <SectionHeader
         title="Add skill"
          description="Capture a skill and attach a practice baseline."
           />
      <div className="form-grid">
        <label>
          Skill name
          <input name="name" value={form.name} onChange={onChange} required />
        </label>
        <label>
          Category
          <select name="category" value={form.category} onChange={onChange}>
            <option>Backend</option>
            <option>Frontend</option>
            <option>Database</option>
            <option>DevOps</option>
            <option>Testing</option>
            <option>System Design</option>
          </select>
        </label>
        <label>
          Level
          <select name="proficiencyLevel" value={form.proficiencyLevel} onChange={onChange}>
            <option>BEGINNER</option>
            <option>INTERMEDIATE</option>
            <option>ADVANCED</option>
            <option>EXPERT</option>
          </select>
        </label>
        <label>
          Confidence score
          <input name="confidenceScore" type="number" min="1" max="100" value={form.confidenceScore} onChange={onChange} required />
        </label>
        <label>
          Target score
          <input name="targetScore" type="number" min="1" max="100" value={form.targetScore} onChange={onChange} required />
        </label>
        <label>
          Priority rank
          <input name="priorityRank" type="number" min="1" max="10" value={form.priorityRank} onChange={onChange} required />
        </label>
        <label>
          Last practiced
          <input name="lastPracticedOn" type="date" value={form.lastPracticedOn} onChange={onChange} />
        </label>
        <label className="form-grid__wide">
          Learning note
          <textarea name="learningNote" rows="3" value={form.learningNote} onChange={onChange} />
        </label>
      </div>
      <div className="action-row">
        <button className="primary-button" type="submit" disabled={loading}>
          {loading ? "Saving..." : editingId ? "Update skill" : "Add skill"}
        </button>
        {editingId ? (
          <button className="secondary-button" type="button" onClick={onCancel}>
            Cancel
          </button>
        ) : null}
      </div>
    </form>
  );
}

function GoalForm({ form, editingId, skills, onChange, onSubmit, onCancel, loading }) {
  return (
    <form className="panel panel--form" onSubmit={onSubmit}>
            <SectionHeader
             title="Create goal"
             description="Turn intention into a visible deadline and outcome."
              />

      <div className="form-grid">
        <label>
          Skill
          <select name="skillId" value={form.skillId} onChange={onChange} required>
            <option value="">Select a skill</option>
            {skills.map((skill) => (
              <option key={skill.id} value={skill.id}>
                {skill.name}
              </option>
            ))}
          </select>
        </label>
        <label>
          Status
          <select name="status" value={form.status} onChange={onChange}>
            <option>PLANNED</option>
            <option>IN_PROGRESS</option>
            <option>COMPLETED</option>
            <option>ON_HOLD</option>
          </select>
        </label>
        <label className="form-grid__wide">
          Goal title
          <input name="title" value={form.title} onChange={onChange} required />
        </label>
        <label className="form-grid__wide">
          Description
          <textarea name="description" rows="3" value={form.description} onChange={onChange} required />
        </label>
        <label>
          Target date
          <input name="targetDate" type="date" value={form.targetDate} onChange={onChange} required />
        </label>
      </div>
      <div className="action-row">
        <button className="primary-button" type="submit" disabled={loading || skills.length === 0}>
          {loading ? "Saving..." : editingId ? "Update goal" : "Create goal"}
        </button>
        {editingId ? (
          <button className="secondary-button" type="button" onClick={onCancel}>
            Cancel
          </button>
        ) : null}
      </div>
    </form>
  );
}

function RoadmapForm({ form, editingId, onChange, onSubmit, onCancel, loading }) {
  return (
    <form className="panel panel--form" onSubmit={onSubmit}>
      <SectionHeader
        title={editingId ? "Edit Roadmap" : "Generate Roadmap"}
        description="Create a personalized learning roadmap inspired by your original Node project."
      />
      <div className="form-grid">
        <label className="form-grid__wide">
          Roadmap title
          <input name="title" value={form.title} onChange={onChange} required />
        </label>
        <label className="form-grid__wide">
          Learning goal
          <textarea name="goal" rows="3" value={form.goal} onChange={onChange} required />
        </label>
        <label>
          Current level
          <select name="currentLevel" value={form.currentLevel} onChange={onChange}>
            <option>Beginner</option>
            <option>Intermediate</option>
            <option>Advanced</option>
          </select>
        </label>
        <label>
          Target level
          <select name="targetLevel" value={form.targetLevel} onChange={onChange}>
            <option>Intermediate</option>
            <option>Advanced</option>
            <option>Expert</option>
          </select>
        </label>
      </div>
      <div className="action-row">
        <button className="primary-button" type="submit" disabled={loading}>
          {loading ? "Saving..." : editingId ? "Update roadmap" : "Create roadmap"}
        </button>
        {editingId ? (
          <button className="secondary-button" type="button" onClick={onCancel}>
            Cancel
          </button>
        ) : null}
      </div>
    </form>
  );
}

function SkillsTable({ skills, onEdit, onDelete, deletingId }) {
  return (
    <section className="panel">
      <SectionHeader title="Tracked Skills" description="A clean view of your current skill inventory." />
      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Skill</th>
              <th>Category</th>
              <th>Level</th>
              <th>Confidence</th>
              <th>Target</th>
              <th>Last practiced</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {skills.map((skill) => (
              <tr key={skill.id}>
                <td>
                  <strong>{skill.name}</strong>
                  <span className="table-note">{skill.learningNote || "No note yet"}</span>
                </td>
                <td>{skill.category}</td>
                <td>{skill.proficiencyLevel}</td>
                <td>{skill.confidenceScore}%</td>
                <td>{skill.targetScore}%</td>
                 <td>{skill.lastPracticedOn ? formatLongDate(skill.lastPracticedOn) : "Not set"}</td>
                <td>
                  <div className="table-actions">
                    <button className="table-button" type="button" onClick={() => onEdit(skill)}>
                      Edit
                    </button>
                    <button className="table-button table-button--danger" type="button" onClick={() => onDelete(skill.id)} disabled={deletingId === skill.id}>
                      {deletingId === skill.id ? "Deleting..." : "Delete"}
                    </button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}

function GoalList({ goals, onEdit, onDelete, deletingId }) {
  return (
    <section className="panel">
      <SectionHeader title="Goals" description="Deadline-based milestones that keep your roadmap honest." />
      <div className="stack-list">
         {goals.length === 0 ? (
                  <div className="empty-state">
                    <strong>No goals yet</strong>
                    <p>Create your first goal to turn your growth plan into a concrete target.</p>
                  </div>
                ) : (
                  goals.map((goal) => {
                    const remainingDays = daysUntil(goal.targetDate);
                    return (
                      <article className="goal-card" key={goal.id}>
                        <div>
                          <span className={`badge badge--${goal.status.toLowerCase()}`}>{goal.status.replace("_", " ")}</span>
                          <h3>{goal.title}</h3>
                          <p>{goal.description}</p>
                        </div>
                        <div className="goal-card__aside">
                          <span>{goal.skillName}</span>
                          <strong>{formatLongDate(goal.targetDate)}</strong>
                          <small>{remainingDays === null ? "" : remainingDays >= 0 ? `${remainingDays} days left` : `${Math.abs(remainingDays)} days overdue`}</small>
                        </div>
                      </article>
                    );
                  })
                )}
      </div>
    </section>
  );
}

function RoadmapPanel({ roadmaps, onEdit, onDelete, onToggle, deletingId }) {
  return (
    <section className="panel">
      <SectionHeader title="Personalized Roadmaps" description="Break a big learning target into clear, trackable steps." />
      {roadmaps.length === 0 ? (
        <div className="empty-state">
          <strong>No roadmap yet</strong>
          <p>Create one above and the backend will save it in PostgreSQL for your account.</p>
        </div>
      ) : (
        <div className="stack-list">
          {roadmaps.map((roadmap) => (
            <article className="roadmap-card" key={roadmap.id}>
              <div className="roadmap-card__header">
                <div>
                  <span className="chip">{roadmap.currentLevel} to {roadmap.targetLevel}</span>
                  <h3>{roadmap.title}</h3>
                  <p>{roadmap.introduction}</p>
                </div>
                <div className="roadmap-card__tools">
                  <strong>{roadmap.progress}% complete</strong>
                  <div className="action-row action-row--compact">
                    <button className="table-button" type="button" onClick={() => onEdit(roadmap)}>
                      Edit
                    </button>
                    <button className="table-button table-button--danger" type="button" onClick={() => onDelete(roadmap.id)} disabled={deletingId === roadmap.id}>
                      {deletingId === roadmap.id ? "Deleting..." : "Delete"}
                    </button>
                  </div>
                </div>
              </div>

              <div className="progress-bar" aria-hidden="true">
                <span style={{ width: `${roadmap.progress}%` }} />
              </div>

              <div className="roadmap-step-list">
                {roadmap.steps.map((step) => (
                  <label className="roadmap-step" key={step.id}>
                    <input type="checkbox" checked={step.completed} onChange={() => onToggle(roadmap.id, step.id)} />
                    <div>
                      <strong>{step.stepOrder}. {step.title}</strong>
                      <p>{step.description}</p>
                      {step.resources.map((resource) => (
                        <a key={resource.id} href={resource.url} rel="noreferrer" target="_blank">
                          {resource.type}: {resource.title}
                        </a>
                      ))}
                    </div>
                  </label>
                ))}
              </div>

              <div className="roadmap-columns">
                <div>
                  <h4>Projects</h4>
                  <ul className="insight-list">
                    {roadmap.projects.map((project) => (
                      <li key={project}>{project}</li>
                    ))}
                  </ul>
                </div>
                <div>
                  <h4>Tips</h4>
                  <ul className="insight-list">
                    {roadmap.tips.map((tip) => (
                      <li key={tip}>{tip}</li>
                    ))}
                  </ul>
                </div>
              </div>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

function AssessmentPanel({ assessments, activeAssessmentId, answers, onStart, onSelect, onSubmit, result }) {
  const active = assessments.find((assessment) => assessment.id === activeAssessmentId) || null;

  return (
    <section className="panel">
      <SectionHeader title="Assessments" description="Test what you know and let practice data update your confidence ." />
      <div className="assessment-layout">
        <div className="assessment-list">
          {assessments.length === 0 ? (
                     <div className="empty-state">
                       <strong>No assessments yet</strong>
                       <p>Add a skill and the workspace will generate a starter assessment.</p>
                     </div>
                   ) : (
                     assessments.map((assessment) => (
                       <button
                         className={`assessment-card ${assessment.id === activeAssessmentId ? "assessment-card--active" : ""}`}
                         key={assessment.id}
                         onClick={() => onStart(assessment.id)}
                         type="button"
                       >
                         <span>{assessment.skillName}</span>
                         <strong>{assessment.title}</strong>
                         <small>
                           {assessment.difficulty} • {assessment.questionCount} questions
                         </small>
                       </button>
                     ))
                   )}
        </div>

        <div className="assessment-detail">
          {active ? (
            <form
              onSubmit={(event) => {
                event.preventDefault();
                onSubmit(active.id);
              }}
            >
              <span className="eyebrow eyebrow--soft">Active assessment</span>
              <h3>{active.title}</h3>
              <p className="muted-copy">Answer every question, submit once, and review your training advice.</p>
              <div className="stack-list">
                {active.questions.map((question, index) => (
                  <article className="question-card" key={question.id}>
                    <strong>{index + 1}. {question.prompt}</strong>
                    {["A", "B", "C", "D"].map((option) => (
                      <label key={option}>
                        <input
                          type="radio"
                          name={`question-${question.id}`}
                          checked={answers[question.id] === option}
                          onChange={() => onSelect(question.id, option)}
                        />
                        {question[`option${option}`]}
                      </label>
                    ))}
                  </article>
                ))}
              </div>
              <button className="primary-button" type="submit">
                Submit assessment
              </button>
              {result ? (
                <div className="result-card">
                  <strong>Latest result: {result.score}/{result.totalQuestions}</strong>
                  <p>{result.feedback}</p>
                </div>
              ) : null}
            </form>
          ) : (
            <div className="empty-state">
              <strong>Select an assessment</strong>
              <p>Your tests live here. Complete one to update your tracked growth.</p>
            </div>
          )}
        </div>
      </div>
    </section>
  );
}

function TrainingPanel({ resources }) {
  return (
    <section className="panel">
      <SectionHeader title="Training Queue" description="Actionable resources to help you close the next gap." />
      <div className="training-grid">
         {resources.length === 0 ? (
                  <div className="empty-state">
                    <strong>No resources yet</strong>
                    <p>Add skills and assessments to generate personalized training recommendations.</p>
                  </div>
                ) : (
                  resources.map((resource) => (
                    <article className="training-card" key={resource.id}>
                      <span className="chip">{resource.skillName}</span>
                      <h3>{resource.title}</h3>
                      <p>{resource.description}</p>
                      <div className="training-card__footer">
                        <span>{resource.type}</span>
                        <span>{resource.estimatedMinutes} min</span>
                      </div>
                      <a href={resource.url} target="_blank" rel="noreferrer">
                        Open resource
                      </a>
                    </article>
                  ))
                )}
      </div>
    </section>
  );
}

function InsightsPanel({ items }) {
  return (
    <section className="panel">
      <SectionHeader title="Insights" description="Signals pulled from your goals, tests, and training data." />
      <ul className="insight-list">
        {items.map((item) => (
           <article className="insight-card" key={item}>
                      <span className="eyebrow eyebrow--soft">Signal</span>
                      <p>{item}</p>
                    </article>
        ))}
      </ul>
    </section>
  );
}

function parseNumericFormValue(name, value) {
  const numericFields = new Set(["confidenceScore", "targetScore", "priorityRank", "skillId"]);
  return numericFields.has(name) && value !== "" ? Number(value) : value;
}

function normalizeSkillForm(skill) {
  return {
    name: skill.name,
    category: skill.category,
    proficiencyLevel: skill.proficiencyLevel,
    confidenceScore: skill.confidenceScore,
    targetScore: skill.targetScore,
    priorityRank: skill.priorityRank,
    lastPracticedOn: skill.lastPracticedOn || "",
    learningNote: skill.learningNote || ""
  };
}

function normalizeGoalForm(goal) {
  return {
    skillId: goal.skillId,
    title: goal.title,
    description: goal.description,
    targetDate: goal.targetDate,
    status: goal.status
  };
}

function normalizeRoadmapForm(roadmap) {
  return {
    title: roadmap.title,
    goal: roadmap.goal,
    currentLevel: roadmap.currentLevel,
    targetLevel: roadmap.targetLevel
  };
}

export default function App() {
  const [token, setToken] = useState(() => localStorage.getItem("SkillTrackerToken") || "");
  const [authMode, setAuthMode] = useState("login");
  const [authForm, setAuthForm] = useState({ fullName: "", email: "", password: "" });
  const [authLoading, setAuthLoading] = useState(false);
  const [authError, setAuthError] = useState("");

  const [profile, setProfile] = useState(null);
  const [dashboard, setDashboard] = useState(null);
  const [skills, setSkills] = useState([]);
  const [goals, setGoals] = useState([]);
  const [assessments, setAssessments] = useState([]);
  const [training, setTraining] = useState([]);
  const [roadmaps, setRoadmaps] = useState([]);
  const [pageError, setPageError] = useState("");

  const [skillForm, setSkillForm] = useState(initialSkillForm);
  const [goalForm, setGoalForm] = useState(initialGoalForm);
  const [roadmapForm, setRoadmapForm] = useState(initialRoadmapForm);
  const [editingSkillId, setEditingSkillId] = useState(null);
  const [editingGoalId, setEditingGoalId] = useState(null);
  const [editingRoadmapId, setEditingRoadmapId] = useState(null);

  const [savingSkill, setSavingSkill] = useState(false);
  const [savingGoal, setSavingGoal] = useState(false);
  const [savingRoadmap, setSavingRoadmap] = useState(false);
  const [deletingSkillId, setDeletingSkillId] = useState(null);
  const [deletingGoalId, setDeletingGoalId] = useState(null);
  const [deletingRoadmapId, setDeletingRoadmapId] = useState(null);
  const [activeAssessmentId, setActiveAssessmentId] = useState(null);
  const [assessmentAnswers, setAssessmentAnswers] = useState({});
  const [assessmentResult, setAssessmentResult] = useState(null);

  async function loadWorkspace(activeToken) {
    const [profileData, dashboardData, skillData, goalData, assessmentData, trainingData, roadmapData] = await Promise.all([
      getProfile(activeToken),
      getDashboard(activeToken),
      getSkills(activeToken),
      getGoals(activeToken),
      getAssessments(activeToken),
      getTraining(activeToken),
      getRoadmaps(activeToken)
    ]);

    setProfile(profileData);
    setDashboard(dashboardData);
    setSkills(skillData);
    setGoals(goalData);
    setAssessments(assessmentData);
    setTraining(trainingData);
    setRoadmaps(roadmapData);
    setGoalForm((current) => ({ ...current, skillId: current.skillId || skillData[0]?.id || "" }));
    if (!activeAssessmentId && assessmentData[0]) {
      setActiveAssessmentId(assessmentData[0].id);
    }
  }

  useEffect(() => {
    if (!token) {
      return;
    }

    loadWorkspace(token).catch((error) => {
      setPageError(error.message);
      localStorage.removeItem("SkillTrackerToken");
      setToken("");
    });
  }, [token]);

  function handleAuthFieldChange(event) {
    const { name, value } = event.target;
    setAuthForm((current) => ({ ...current, [name]: value }));
  }

  async function handleAuthSubmit(event) {
    event.preventDefault();
    setAuthLoading(true);
    setAuthError("");

    try {
            const response = authMode === "login" ? await login(authForm) : await register(authForm);

      localStorage.setItem("SkillTrackerToken", response.token);
      setToken(response.token);
      setProfile(response.user);
    } catch (error) {
      setAuthError(error.message);
    } finally {
      setAuthLoading(false);
    }
  }

  function handleSkillFieldChange(event) {
    const { name, value } = event.target;
    setSkillForm((current) => ({ ...current, [name]: parseNumericFormValue(name, value) }));
  }

  function handleGoalFieldChange(event) {
    const { name, value } = event.target;
    setGoalForm((current) => ({ ...current, [name]: parseNumericFormValue(name, value) }));
  }

  function handleRoadmapFieldChange(event) {
    const { name, value } = event.target;
    setRoadmapForm((current) => ({ ...current, [name]: value }));
  }

  function resetSkillForm() {
    setSkillForm(initialSkillForm);
    setEditingSkillId(null);
  }

  function resetGoalForm() {
    setGoalForm((current) => ({ ...initialGoalForm, skillId: current.skillId || skills[0]?.id || "" }));
    setEditingGoalId(null);
  }

  function resetRoadmapForm() {
    setRoadmapForm(initialRoadmapForm);
    setEditingRoadmapId(null);
  }

  async function handleSaveSkill(event) {
    event.preventDefault();
    setSavingSkill(true);
    setPageError("");

    try {
      if (editingSkillId) {
        await updateSkill(token, editingSkillId, skillForm);
      } else {
        await createSkill(token, skillForm);
      }
      resetSkillForm();
      await loadWorkspace(token);
    } catch (error) {
      setPageError(error.message);
    } finally {
      setSavingSkill(false);
    }
  }

  async function handleSaveGoal(event) {
    event.preventDefault();
    setSavingGoal(true);
    setPageError("");

    try {
      if (editingGoalId) {
        await updateGoal(token, editingGoalId, goalForm);
      } else {
        await createGoal(token, goalForm);
      }
      resetGoalForm();
      await loadWorkspace(token);
    } catch (error) {
      setPageError(error.message);
    } finally {
      setSavingGoal(false);
    }
  }

  async function handleSaveRoadmap(event) {
    event.preventDefault();
    setSavingRoadmap(true);
    setPageError("");

    try {
      if (editingRoadmapId) {
        await updateRoadmap(token, editingRoadmapId, roadmapForm);
      } else {
        await createRoadmap(token, roadmapForm);
      }
      resetRoadmapForm();
      await loadWorkspace(token);
    } catch (error) {
      setPageError(error.message);
    } finally {
      setSavingRoadmap(false);
    }
  }

  async function handleDeleteSkill(skillId) {
    setDeletingSkillId(skillId);
    setPageError("");
    try {
      await deleteSkill(token, skillId);
      if (editingSkillId === skillId) {
        resetSkillForm();
      }
      await loadWorkspace(token);
    } catch (error) {
      setPageError(error.message);
    } finally {
      setDeletingSkillId(null);
    }
  }

  async function handleDeleteGoal(goalId) {
    setDeletingGoalId(goalId);
    setPageError("");
    try {
      await deleteGoal(token, goalId);
      if (editingGoalId === goalId) {
        resetGoalForm();
      }
      await loadWorkspace(token);
    } catch (error) {
      setPageError(error.message);
    } finally {
      setDeletingGoalId(null);
    }
  }

  async function handleDeleteRoadmap(roadmapId) {
    setDeletingRoadmapId(roadmapId);
    setPageError("");
    try {
      await deleteRoadmap(token, roadmapId);
      if (editingRoadmapId === roadmapId) {
        resetRoadmapForm();
      }
      await loadWorkspace(token);
    } catch (error) {
      setPageError(error.message);
    } finally {
      setDeletingRoadmapId(null);
    }
  }

  function handleAssessmentAnswer(questionId, selectedOption) {
    setAssessmentAnswers((current) => ({ ...current, [questionId]: selectedOption }));
  }

  async function handleAssessmentSubmit(assessmentId) {
    const active = assessments.find((assessment) => assessment.id === assessmentId);
    if (!active) {
      return;
    }

    const hasUnanswered = active.questions.some((question) => !assessmentAnswers[question.id]);
    if (hasUnanswered) {
      setPageError("Answer every question before submitting the assessment.");
      return;
    }

    const answers = active.questions.map((question) => ({
      questionId: question.id,
      selectedOption: assessmentAnswers[question.id]
    }));

    try {
      setPageError("");
      const result = await submitAssessment(token, assessmentId, answers);
      setAssessmentResult(result);
      await loadWorkspace(token);
    } catch (error) {
      setPageError(error.message);
    }
  }

  async function handleRoadmapToggle(roadmapId, stepId) {
    try {
      setPageError("");
      const updatedRoadmap = await toggleRoadmapStep(token, roadmapId, stepId);
      setRoadmaps((current) => current.map((item) => (item.id === updatedRoadmap.id ? updatedRoadmap : item)));
    } catch (error) {
      setPageError(error.message);
    }
  }

  function handleLogout() {
    localStorage.removeItem("SkillTrackerToken");
    setToken("");
    setProfile(null);
    setDashboard(null);
    setSkills([]);
    setGoals([]);
    setAssessments([]);
    setTraining([]);
    setRoadmaps([]);
    setAssessmentResult(null);
    setAssessmentAnswers({});
  }

  if (!token) {
    return (
      <main className="auth-shell">
        <AuthPanel
          mode={authMode}
          form={authForm}
          onChange={handleAuthFieldChange}
          onSubmit={handleAuthSubmit}
          onSwitch={() => setAuthMode((current) => (current === "login" ? "register" : "login"))}
          loading={authLoading}
          error={authError}
        />
      </main>
    );
  }

  if (!dashboard || !profile) {
    return (
      <main className="app-shell loading-shell">
        <p>Loading your workspace...</p>
      </main>
    );
  }

  return (
    <main className="app-shell">
      <section className="hero">
        <div className="hero__copy">
           <span className="eyebrow">Growth command center</span>
                   <h1>{profile.fullName}, keep your learning streak visible and intentional.</h1>
          <p>
            Every skill, roadmap, goal, assessment, and training resource in this dashboard belongs
            to your account and updates through the Spring Boot API.
          </p>
          <div className="hero__callouts">
                      <div>
                        <span>Current streak</span>
                        <strong>{dashboard.dailyGrowth.currentStreak} days</strong>
                      </div>
                      <div>
                        <span>Average confidence</span>
                        <strong>{Math.round(dashboard.averageConfidence)}%</strong>
                      </div>
                    </div>
        </div>
        <div className="hero__actions">
           <div className="hero__profile-card">
                      <span className="eyebrow eyebrow--soft">Workspace owner</span>
            <strong>{dashboard.userName}</strong>
             <span>{profile.email}</span>
                        <p>{dashboard.dailyGrowth.nextMilestone}</p>
          </div>
          <button className="secondary-button" onClick={handleLogout} type="button">
            Sign out
          </button>
        </div>
      </section>

      {pageError ? <p className="page-error">{pageError}</p> : null}

      <section className="stats-grid">
         <StatCard label="Tracked skills" value={dashboard.totalSkills} helper="Owned in your workspace" tone="gold" />
                <StatCard label="Active goals" value={dashboard.activeGoals} helper="Still in motion" tone="teal" />
                <StatCard label="Assessments taken" value={dashboard.assessmentsTaken} helper="Practice history recorded" tone="coral" />
        <StatCard  label="Weekly activity"
                            value={`${dashboard.dailyGrowth.activeDaysThisWeek}/${dashboard.dailyGrowth.weeklyTarget}`}
                            helper="Days active this week"
                            tone="blue"
                            />
      </section>
        <GrowthPanel dailyGrowth={dashboard.dailyGrowth} />

            <section className="workspace-grid workspace-grid--dashboard">
              <FocusSkillsPanel skills={dashboard.focusSkills} />
              <InsightsPanel items={dashboard.insights} />
            </section>

      <section className="workspace-grid workspace-grid--triple">
        <SkillForm
          form={skillForm}
          editingId={editingSkillId}
          onChange={handleSkillFieldChange}
          onSubmit={handleSaveSkill}
          onCancel={resetSkillForm}
          loading={savingSkill}
        />
        <GoalForm
          form={goalForm}
          editingId={editingGoalId}
          skills={skills}
          onChange={handleGoalFieldChange}
          onSubmit={handleSaveGoal}
          onCancel={resetGoalForm}
          loading={savingGoal}
        />
        <RoadmapForm
          form={roadmapForm}
          editingId={editingRoadmapId}
          onChange={handleRoadmapFieldChange}
          onSubmit={handleSaveRoadmap}
          onCancel={resetRoadmapForm}
          loading={savingRoadmap}
        />
      </section>

     <section className="workspace-grid workspace-grid--dashboard">
        <SkillsTable
          skills={skills}
          onEdit={(skill) => {
            setEditingSkillId(skill.id);
            setSkillForm(normalizeSkillForm(skill));
          }}
          onDelete={handleDeleteSkill}
          deletingId={deletingSkillId}
        />
          <GoalList goals={goals} />
      </section>

      <GoalList
        goals={goals}
        onEdit={(goal) => {
          setEditingGoalId(goal.id);
          setGoalForm(normalizeGoalForm(goal));
        }}
        onDelete={handleDeleteGoal}
        deletingId={deletingGoalId}
      />

      <RoadmapPanel
        roadmaps={roadmaps}
        onEdit={(roadmap) => {
          setEditingRoadmapId(roadmap.id);
          setRoadmapForm(normalizeRoadmapForm(roadmap));
        }}
        onDelete={handleDeleteRoadmap}
        onToggle={handleRoadmapToggle}
        deletingId={deletingRoadmapId}
      />

      <AssessmentPanel
        assessments={assessments}
        activeAssessmentId={activeAssessmentId}
        answers={assessmentAnswers}
        onStart={(assessmentId) => {
          setActiveAssessmentId(assessmentId);
          setAssessmentResult(null);
          setPageError("");
        }}
        onSelect={handleAssessmentAnswer}
        onSubmit={handleAssessmentSubmit}
        result={assessmentResult}
      />

      <TrainingPanel resources={training} />
    </main>
  );
}
