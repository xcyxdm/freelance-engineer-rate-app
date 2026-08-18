package com.example.interviewreport;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InterviewReportController {

    private static final List<String> ROLE_OPTIONS = List.of(
            "SE・プログラマ・開発エンジニア",
            "ネットワーク・サーバーエンジニア",
            "セキュリティエンジニア",
            "PMO・SEアシスタント",
            "テストエンジニア（評価・検証）",
            "ヘルプデスク・テクニカルサポート・OAインストラクター",
            "FAE・セールスエンジニア・プリセールス",
            "AIエンジニア・データサイエンティスト"
    );

    private static final List<AgeGroup> AGE_GROUPS = List.of(
            new AgeGroup("20s", "20代", 20, 29),
            new AgeGroup("30s", "30代", 30, 39),
            new AgeGroup("40s", "40代", 40, 49),
            new AgeGroup("50s", "50代", 50, 59),
            new AgeGroup("60plus", "60代以上", 60, 999)
    );

    private final InterviewReportRepository repository;

    public InterviewReportController(InterviewReportRepository repository) {
        this.repository = repository;
    }

    @ModelAttribute("contractTypes")
    public ContractType[] contractTypes() {
        return ContractType.values();
    }

    @ModelAttribute("assignedRoles")
    public AssignedRole[] assignedRoles() {
        return AssignedRole.values();
    }

    @ModelAttribute("workStyles")
    public WorkStyle[] workStyles() {
        return WorkStyle.values();
    }

    @ModelAttribute("roleOptions")
    public List<String> roleOptions() {
        return ROLE_OPTIONS;
    }

    @ModelAttribute("ageGroups")
    public List<AgeGroup> ageGroups() {
        return AGE_GROUPS;
    }

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String ageGroup,
            @RequestParam(required = false) String workStyle,
            @RequestParam(required = false) String role,
            Model model
    ) {
        List<InterviewReport> allReports = repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        WorkStyle selectedWorkStyle = parseWorkStyle(workStyle);
        List<InterviewReport> reports = filterReports(allReports, ageGroup, selectedWorkStyle, role);

        model.addAttribute("reports", reports);
        model.addAttribute("ageStats", countByAgeGroup(reports));
        model.addAttribute("workStyleStats", countByWorkStyle(reports));
        model.addAttribute("roleStats", countByRole(reports));
        model.addAttribute("averageUnitPrice", averageUnitPrice(reports));
        model.addAttribute("selectedAgeGroup", ageGroup);
        model.addAttribute("selectedWorkStyle", selectedWorkStyle);
        model.addAttribute("selectedRole", role);
        model.addAttribute("resultCount", reports.size());
        return "index";
    }

    @GetMapping("/reports/new")
    public String newReport(Model model) {
        model.addAttribute("form", new InterviewReportForm());
        model.addAttribute("pageTitle", "フリーエンジニアの相場");
        model.addAttribute("action", "/reports");
        return "form";
    }

    @PostMapping("/reports")
    public String create(
            @Valid @ModelAttribute("form") InterviewReportForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "フリーエンジニアの相場");
            model.addAttribute("action", "/reports");
            return "form";
        }

        InterviewReport report = new InterviewReport();
        form.copyTo(report);
        repository.save(report);
        redirectAttributes.addFlashAttribute("message", "登録しました。");
        return "redirect:/";
    }

    @GetMapping("/reports/{id}")
    public String show(@PathVariable Long id, Model model) {
        InterviewReport report = findReport(id);
        model.addAttribute("report", report);
        return "show";
    }

    @GetMapping("/reports/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        InterviewReport report = findReport(id);
        model.addAttribute("form", InterviewReportForm.fromEntity(report));
        model.addAttribute("pageTitle", "フリーエンジニアの相場");
        model.addAttribute("action", "/reports/" + id);
        return "form";
    }

    @PostMapping("/reports/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("form") InterviewReportForm form,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "フリーエンジニアの相場");
            model.addAttribute("action", "/reports/" + id);
            return "form";
        }

        InterviewReport report = findReport(id);
        form.copyTo(report);
        repository.save(report);
        redirectAttributes.addFlashAttribute("message", "更新しました。");
        return "redirect:/reports/" + id;
    }

    @PostMapping("/reports/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        repository.delete(findReport(id));
        redirectAttributes.addFlashAttribute("message", "削除しました。");
        return "redirect:/";
    }

    private InterviewReport findReport(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found: " + id));
    }

    private WorkStyle parseWorkStyle(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return WorkStyle.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private List<InterviewReport> filterReports(
            List<InterviewReport> reports,
            String ageGroupKey,
            WorkStyle workStyle,
            String role
    ) {
        AgeGroup selectedAgeGroup = AGE_GROUPS.stream()
                .filter(group -> group.getKey().equals(ageGroupKey))
                .findFirst()
                .orElse(null);

        return reports.stream()
                .filter(report -> selectedAgeGroup == null || selectedAgeGroup.contains(report.getAge()))
                .filter(report -> workStyle == null || workStyle == report.getWorkStyle())
                .filter(report -> role == null || role.isBlank() || role.equals(report.getRole()))
                .toList();
    }

    private List<StatRow> countByAgeGroup(List<InterviewReport> reports) {
        List<StatRow> rows = new ArrayList<>();
        for (AgeGroup ageGroup : AGE_GROUPS) {
            long count = reports.stream()
                    .filter(report -> ageGroup.contains(report.getAge()))
                    .count();
            rows.add(new StatRow(ageGroup.getLabel(), count));
        }
        return rows;
    }

    private List<StatRow> countByWorkStyle(List<InterviewReport> reports) {
        List<StatRow> rows = new ArrayList<>();
        for (WorkStyle workStyle : WorkStyle.values()) {
            long count = reports.stream()
                    .filter(report -> workStyle == report.getWorkStyle())
                    .count();
            rows.add(new StatRow(workStyle.getLabel(), count));
        }
        return rows;
    }

    private List<StatRow> countByRole(List<InterviewReport> reports) {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (String role : ROLE_OPTIONS) {
            counts.put(role, 0L);
        }
        for (InterviewReport report : reports) {
            String role = report.getRole();
            if (role != null && !role.isBlank()) {
                counts.put(role, counts.getOrDefault(role, 0L) + 1);
            }
        }
        return counts.entrySet().stream()
                .map(entry -> new StatRow(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(StatRow::getCount).reversed().thenComparing(StatRow::getLabel))
                .toList();
    }

    private String averageUnitPrice(List<InterviewReport> reports) {
        return reports.stream()
                .filter(report -> report.getUnitPrice() != null)
                .mapToInt(InterviewReport::getUnitPrice)
                .average()
                .stream()
                .mapToObj(value -> String.format("%.1f", value))
                .findFirst()
                .orElse("-");
    }

    public static class AgeGroup {
        private final String key;
        private final String label;
        private final int min;
        private final int max;

        public AgeGroup(String key, String label, int min, int max) {
            this.key = key;
            this.label = label;
            this.min = min;
            this.max = max;
        }

        public boolean contains(Integer age) {
            return age != null && min <= age && age <= max;
        }

        public String getKey() {
            return key;
        }

        public String getLabel() {
            return label;
        }
    }

    public static class StatRow {
        private final String label;
        private final long count;

        public StatRow(String label, long count) {
            this.label = label;
            this.count = count;
        }

        public String getLabel() {
            return label;
        }

        public long getCount() {
            return count;
        }
    }
}
