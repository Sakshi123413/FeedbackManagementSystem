export interface Feedback {
  id: string
  userName: string
  title: string
  message: string
  rating: number
  date: string
}

// Mock data for demo purposes
const mockFeedbacks: Feedback[] = [
  {
    id: "1",
    userName: "Sarah Chen",
    title: "Excellent user experience",
    message: "The dashboard is intuitive and the analytics are very helpful for tracking our team performance.",
    rating: 5,
    date: "2026-05-14",
  },
  {
    id: "2",
    userName: "Marcus Johnson",
    title: "Great product, minor improvements needed",
    message: "Overall very satisfied with the platform. Would love to see more export options for reports.",
    rating: 4,
    date: "2026-05-13",
  },
  {
    id: "3",
    userName: "Emily Rodriguez",
    title: "Responsive support team",
    message: "Had an issue with integration and the support team resolved it within hours. Impressive service.",
    rating: 5,
    date: "2026-05-12",
  },
  {
    id: "4",
    userName: "David Park",
    title: "Good but could be faster",
    message: "The features are solid but page loading times could be improved for better workflow efficiency.",
    rating: 3,
    date: "2026-05-11",
  },
  {
    id: "5",
    userName: "Lisa Thompson",
    title: "Perfect for our team",
    message: "We switched from a competitor and this has been significantly better for our feedback collection needs.",
    rating: 5,
    date: "2026-05-10",
  },
]

let feedbacks = [...mockFeedbacks]

export async function getFeedbacks(): Promise<Feedback[]> {
  // Simulate API delay
  await new Promise(resolve => setTimeout(resolve, 600))
  return [...feedbacks].sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime())
}

export async function submitFeedback(data: {
  title: string
  message: string
  rating: number
  userName: string
}): Promise<Feedback> {
  // Simulate API delay
  await new Promise(resolve => setTimeout(resolve, 800))

  const newFeedback: Feedback = {
    id: Math.random().toString(36).substring(7),
    userName: data.userName,
    title: data.title,
    message: data.message,
    rating: data.rating,
    date: new Date().toISOString().split("T")[0],
  }

  feedbacks = [newFeedback, ...feedbacks]
  return newFeedback
}
