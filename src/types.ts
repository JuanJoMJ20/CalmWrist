export type Severity = "stable" | "elevated" | "crisis";
export type Screen = "panic" | "breathing";

export const BPM_MIN = 60;
export const BPM_MAX = 140;
export const AUTO_TRIGGER_BPM = 100; // dispara Modo Crisis automático
export const CRISIS_BPM = 110;       // tono imperativo
export const STABLE_BPM = 80;        // tono sereno

export const getSeverity = (bpm: number): Severity =>
  bpm > CRISIS_BPM ? "crisis" : bpm < STABLE_BPM ? "stable" : "elevated";
