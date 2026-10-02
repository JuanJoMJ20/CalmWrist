import { useEffect, useState } from "react";
import { motion } from "framer-motion";
import type { Severity } from "../types";

interface Props { bpm: number; severity: Severity; onExit: () => void; }

const PHASE = 4; // segundos por fase (inhala / exhala)

/** Voz = empática y protectora (constante). Tono = cambia según BPM. */
const tone = (s: Severity, inhale: boolean) => {
  if (s === "crisis") return inhale ? "Inhala ahora. Mira el círculo. Estoy contigo." : "Exhala despacio. Sigo aquí contigo.";
  if (s === "elevated") return inhale ? "Inhala suave. Estás a salvo." : "Suelta el aire. Lo estás haciendo bien.";
  return inhale ? "Ritmo estable. Todo está bien." : "Respira con calma. Aquí estoy.";
};

export default function BreathingGuideScreen({ bpm, severity, onExit }: Props) {
  const [inhale, setInhale] = useState(true);
  useEffect(() => {
    const id = setInterval(() => setInhale((v) => !v), PHASE * 1000);
    return () => clearInterval(id);
  }, []);

  const color = severity === "crisis" ? "#ff8a8a" : severity === "elevated" ? "#f4b978" : "#7dd3c0";

  return (
    <div className="relative flex h-full w-full flex-col items-center justify-center">
      <p className="absolute top-[34px] w-[200px] text-center text-[13px] font-medium leading-tight text-white" aria-live="polite">
        {tone(severity, inhale)}
      </p>

      <div className="relative flex h-[170px] w-[170px] items-center justify-center">
        {/* Pulsos visuales: simulan la vibración en la muñeca */}
        {[0, 1].map((i) => (
          <motion.span
            key={i}
            className="absolute rounded-full border-2"
            style={{ borderColor: color, width: 90, height: 90 }}
            animate={{ scale: [1, 1.9], opacity: [0.6, 0] }}
            transition={{ duration: 1.2, repeat: Infinity, delay: i * 0.6, ease: "easeOut" }}
          />
        ))}
        <motion.div
          animate={{ scale: inhale ? 1.7 : 0.8 }}
          transition={{ duration: PHASE, ease: "easeInOut" }}
          className="h-[90px] w-[90px] rounded-full"
          style={{ background: `radial-gradient(circle, ${color}, ${color}22)` }}
        />
        <span className="absolute text-sm font-semibold text-black mix-blend-normal">
          <span className="rounded-full bg-black/60 px-2 py-0.5 text-white">{inhale ? "Inhala" : "Exhala"}</span>
        </span>
      </div>

      <button
        onClick={onExit}
        className="absolute bottom-[26px] flex h-10 min-w-[84px] items-center justify-center rounded-full bg-zinc-800 px-4 text-xs text-zinc-200 focus:outline-none focus-visible:ring-2 focus-visible:ring-white"
      >
        {bpm} BPM · Salir
      </button>
    </div>
  );
}
