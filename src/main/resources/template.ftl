<!DOCTYPE html>
<html lang="en">
<head>
    <title>Students Table Chart</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            padding: 8px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        td.student-points {
            color: #f66262;
        }
        td.checkbox {
            background-color: #fcc2c2;
        }
    </style>
</head>
<body>
<table border="1">
    <tr>
        <th>Group</th>
        <th>Name</th>
        <th>Task</th>
        <th>Soft Deadline</th>
        <th>Hard Deadline</th>
        <th>Build</th>
        <th>Documentation</th>
        <th>Tests total</th>
        <th>Tests passed</th>
        <th>Tests ignored</th>
        <th>Points</th>
    </tr>
    <#list groups as group>
        <#if group.students?size != 0>
            <#list group.students as student>
                <tr>
                <#if student?is_first>
                    <td rowspan="${group.tasks}">${group.name}</td>
                </#if>
                <#if student.assignments?size != 0>
                    <#list student.assignments as assignment>
                        <#if assignment?is_first>
                            <td rowspan="${student.assignments?size}">${student.name}</td>
                        </#if>
                        <td>${assignment.info.title}</td>
                        <td class="checkbox"><input type="checkbox" class="soft-deadline-checkbox" data-points="${assignment.points}" data-student="${student.name}">${assignment.softDeadline} </td>
                        <td class="checkbox"><input type="checkbox" class="hard-deadline-checkbox" data-points="${assignment.points}" data-student="${student.name}">${assignment.hardDeadline} </td>

                        <td class="build">${assignment.build}</td>
                        <td class="docs">${assignment.docs}</td>
                        <td>${assignment.testsTotal}</td>
                        <td>${assignment.testsPassed}</td>
                        <td>${assignment.testsIgnored}</td>
                        <td class="student-points">0</td>
                        </tr>
                    </#list>
                <#else>
                    <td>${student.name}</td>
                    <td colspan="5">Student isn't assigned any tasks</td>
                    </tr>
                </#if>
            </#list>
        <#else>
            <tr>
                <td>${group.name}</td>
                <td colspan="6">The group is empty</td>
            </tr>
        </#if>
    </#list>
</table>

<table border="1">
    <tr>
        <th>Group Name</th>
        <th>Student Name</th>
        <th>Student Activity Percentage</th>
    </tr>
    <#list groups as group>
        <#if group.students?size != 0>
            <#list group.students as student>
                <tr class="student-row-${student.name}">
                    <#if student?is_first>
                        <td rowspan="${group.students?size}">${group.name}</td>
                    </#if>
                    <td>${student.name}</td>
                    <td class="student-activity-percentage">${student.activityPercentage}%</td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td>${group.name}</td>
                <td colspan="6">The group is empty</td>
            </tr>
        </#if>
    </#list>
</table>

<script>

    const softDeadlineCheckboxes = document.querySelectorAll('.soft-deadline-checkbox');
    const hardDeadlineCheckboxes = document.querySelectorAll('.hard-deadline-checkbox');

    function handleCheckboxSoftChange(checkboxes) {
        checkboxes.forEach((checkbox) => {
            checkbox.addEventListener('change', () => {
                const pointsCell = checkbox.parentNode.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling;
                const buildCell = checkbox.parentNode.nextElementSibling.nextElementSibling;
                const buildStatus = buildCell.textContent.trim()

                if (buildStatus === "Failed to build") {
                    return;
                }

                if (checkbox.checked) {
                    pointsCell.textContent = parseFloat(pointsCell.textContent) + 0.5;
                } else {
                    pointsCell.textContent = parseFloat(pointsCell.textContent) - 0.5;
                }

                // Update checkbox color
                checkbox.parentNode.style.backgroundColor = checkbox.checked ? '#c0dcbb' : '#fcc2c2';

                // Update points color
                const points = parseFloat(pointsCell.textContent);
                if (points === 0) {
                    pointsCell.style.color = '#f66262';
                } else if (points === 0.5) {
                    pointsCell.style.color = '#e6a037';
                } else {
                    pointsCell.style.color = '#2da919';
                }


            });
        });
    }

    function handleCheckboxHardChange(checkboxes) {
        checkboxes.forEach((checkbox) => {
            checkbox.addEventListener('change', () => {
                const pointsCell = checkbox.parentNode.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling.nextElementSibling;
                const buildCell = checkbox.parentNode.nextElementSibling;
                const buildStatus = buildCell.textContent.trim()

                if (buildStatus === "Failed to build") {
                    return;
                }

                if (checkbox.checked) {
                    pointsCell.textContent = parseFloat(pointsCell.textContent) + 0.5;
                } else {
                    pointsCell.textContent = parseFloat(pointsCell.textContent) - 0.5;
                }

                // Update checkbox color
                checkbox.parentNode.style.backgroundColor = checkbox.checked ? '#c0dcbb' : '#fcc2c2';

                // Update points color
                const points = parseFloat(pointsCell.textContent);
                if (points === 0) {
                    pointsCell.style.color = '#f66262';
                } else if (points === 0.5) {
                    pointsCell.style.color = '#e6a037';
                } else {
                    pointsCell.style.color = '#2da919';
                }

            });
        });
    }

    function changeActivityPercentageColor() {
        const activityPercentageCells = document.querySelectorAll('.student-activity-percentage');
        activityPercentageCells.forEach((cell) => {
            const activityPercentage = parseFloat(cell.textContent.replace('%', ''));
            if (activityPercentage <= 50) {
                cell.style.color = '#f66262';
            } else if (activityPercentage <= 75) {
                cell.style.color = '#e6a037';
            } else {
                cell.style.color = '#2da919';
            }
        });
    }

    function changeBuildColor() {
        const activityPercentageCells = document.querySelectorAll('.build');
        activityPercentageCells.forEach((cell) => {
            const activityPercentage = cell.textContent.trim();
            if (activityPercentage === "Failed to build") {
                cell.style.color = '#f66262';
            } else {
                cell.style.color = '#2da919';
            }
        });
    }

    function changeDocsColor() {
        const activityPercentageCells = document.querySelectorAll('.docs');
        activityPercentageCells.forEach((cell) => {
            const activityPercentage = cell.textContent.trim();
            if (activityPercentage === "Generated") {
                cell.style.color = '#f66262';
            } else {
                cell.style.color = '#2da919';
            }
        });
    }


    function changeDocsColor() {
        const docs = document.querySelectorAll('.docs');
        docs.forEach((cell) => {
            const doc = cell.textContent.trim();
            if (doc === "Generated") {
                docs.style.color = '#f66262';
            } else {
                docs.style.color = '#2da919';
            }
        });
    }

    handleCheckboxSoftChange(softDeadlineCheckboxes);
    handleCheckboxHardChange(hardDeadlineCheckboxes);
    changeActivityPercentageColor();
    changeBuildColor();
    changeDocsColor();
</script>


</body>
</html>
