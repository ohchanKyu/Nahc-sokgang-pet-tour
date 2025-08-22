import styles from "./DetailTabBar.module.css";

export default function DetailTabBar({ sections, active, onChange }) {
  return (
    <div className={styles.stickyWrap} role="navigation" aria-label="상세 섹션 탭">
      <div className={styles.tabRow}>
        {sections.map(s => (
          <button
            key={s.id}
            className={`${styles.tab} ${active === s.id ? styles.active : ""}`}
            onClick={() => onChange?.(s.id)}
            type="button"
          >
            {s.label}
            {s.count != null && <span className={styles.badge}>{s.count}</span>}
          </button>
        ))}
      </div>
    </div>
  );
}
